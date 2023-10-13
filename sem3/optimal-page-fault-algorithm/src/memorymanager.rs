#![allow(dead_code)]

use std::collections::HashSet;

const PAGE_SIZE: u16 = 256;

type PageNumber = u32;
type FrameNumber = u32;

pub struct MemoryManager {
    pub num_page_faults: u32,
    page_table: [Option<FrameNumber>; PAGE_SIZE as usize],
    page_addresses: Vec<PageNumber>,
    addresses_index: PageNumber,
}

impl MemoryManager {
    pub fn new(physical_addresses: &[u32]) -> Self {
        let page_addresses: Vec<PageNumber> = physical_addresses
            .iter()
            .map(|x| MemoryManager::get_page_number(*x))
            .collect();

        Self {
            page_addresses,
            addresses_index: 0,
            page_table: [None; PAGE_SIZE as usize],
            num_page_faults: 0,
        }
    }

    pub fn read_memory(&mut self, logical_address: u32) {
        let page_number = MemoryManager::get_page_number(logical_address);
        let _page_offset = MemoryManager::get_page_offset(logical_address);

        if self.page_table.get(page_number as usize).unwrap().is_none() {
            self.page_fault(page_number);
        }

        let frame_number = self.page_table.get(page_number as usize).unwrap().unwrap();

        println!("Assigned page {page_number} to frame {frame_number}");

        self.addresses_index += 1;
    }

    fn get_page_number(logical_address: u32) -> u32 {
        return logical_address / PAGE_SIZE as u32;
    }

    fn get_page_offset(logical_address: u32) -> u32 {
        return logical_address % PAGE_SIZE as u32;
    }

    /// Handles a page fault for the specified page number.
    ///
    /// Returns the frame number assigned
    fn page_fault(&mut self, page_number: PageNumber) {
        if self.num_page_faults < PAGE_SIZE as u32 {
            self.page_table[page_number as usize] = Some(self.num_page_faults);
            self.num_page_faults += 1;
            return;
        }

        self.num_page_faults += 1;

        let old_page = self.find_last_page_use();

        // Swap frames in page table
        let frame = self.page_table[old_page as usize];
        self.page_table[old_page as usize] = None;
        self.page_table[page_number as usize] = frame;
    }

    fn find_last_page_use(&self) -> PageNumber {
        let future_addresses =
            &self.page_addresses[self.addresses_index as usize..self.page_addresses.len()];
        find_last_use(future_addresses, &self.page_table)
    }
}

fn find_last_use(
    future_addresses: &[FrameNumber],
    page_table: &[Option<FrameNumber>],
) -> PageNumber {
    let mut earliest_use = vec![None; page_table.len()];
    // pages that will be needed in the future
    let mut needed_pages = HashSet::new();

    // iterate through all future uses and find their earliest use index
    for (index, future_page) in future_addresses.iter().enumerate() {
        // if there isn't already an use index for that page and the page is currently being used
        if (earliest_use
            .get(*future_page as usize)
            .expect("Future pages contain pages bigger than page table"))
        .is_none()
            && (page_table[*future_page as usize]).is_some()
        {
            earliest_use[*future_page as usize] = Some(index);
            needed_pages.insert(*future_page);
        }
    }

    // currently active pages in memory
    let active_pages: Vec<FrameNumber> = page_table
        .iter()
        .enumerate()
        .filter(|&(_, x)| x.is_some())
        .map(|(index, _)| index as PageNumber)
        .collect();

    // filter out any that will be used in the future
    let unused_active_pages: Vec<&u32> = active_pages
        .iter()
        .filter(|x| !needed_pages.contains(x))
        .collect();

    if unused_active_pages.len() > 0 {
        return *unused_active_pages[0];
    }

    // find never used (None in array) or find last use (max number in array)
    let (index, _) = earliest_use
        .iter()
        .enumerate()
        .max_by_key(|&(_, &value)| value)
        .unwrap();

    index as PageNumber
}

#[cfg(test)]
mod tests {
    use super::*;

    fn make_page_table(pages: Vec<u32>) -> Vec<Option<u32>> {
        let max_page = pages.iter().max().unwrap();
        let mut page_table = vec![None; (max_page + 1) as usize];

        for page in pages {
            page_table[page as usize] = Some(1);
        }

        page_table
    }

    #[test]
    fn unused_active_page() {
        let future_pages = vec![7, 1, 0, 5, 6, 3, 7];
        let page_table: Vec<Option<u32>> = make_page_table(vec![0, 1, 2, 7]);
        assert_eq!(find_last_use(&future_pages, &page_table), 2);
    }

    #[test]
    fn multiple_unused_active_pages() {
        let future_pages = vec![3, 0, 4, 2, 3, 0, 3, 2, 3];
        let page_table: Vec<Option<u32>> = make_page_table(vec![0, 1, 2, 7]);
        assert_eq!(find_last_use(&future_pages, &page_table), 1);
    }

    #[test]
    fn last_used_page() {
        let future_pages = vec![3, 3, 2, 2, 0, 0, 0, 1, 0];
        let page_table: Vec<Option<u32>> = make_page_table(vec![0, 1, 2, 3]);
        assert_eq!(find_last_use(&future_pages, &page_table), 1);
    }
}
