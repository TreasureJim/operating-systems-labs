use std::{cmp::Ordering, collections::HashSet};

const PAGE_SIZE: u16 = 256;

type PageNumber = u32;
type FrameNumber = u32;

pub struct MemoryManager {
    pub num_page_faults: u32,
    page_table: [Option<FrameNumber>; PAGE_SIZE as usize],
    page_addresses: Vec<FrameNumber>,
    addresses_index: PageNumber,
}

impl MemoryManager {
    pub fn new(page_addresses: Vec<u32>) -> Self {
        Self {
            page_addresses,
            addresses_index: 0,
            page_table: [None; PAGE_SIZE as usize],
            num_page_faults: 0,
        }
    }

    pub fn read_memory(&mut self, logical_address: u32) {
        self.addresses_index += 1;

        let page_number = MemoryManager::get_page_number(logical_address);
        let page_offset = MemoryManager::get_page_offset(logical_address);

        if self.page_table[page_number as usize] == None {
            self.page_fault(page_number);
        }

        let frame_number = self.page_table[page_number as usize].unwrap();

        println!("Assigned page {page_number} to frame {frame_number}");
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
    fn page_fault(&mut self, page_number: PageNumber) -> FrameNumber {
        self.num_page_faults += 1;

        todo!()
    }

    fn find_last_page_use(&self) -> u32 {
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
        if (earliest_use[*future_page as usize]).is_none()
            && (page_table[*future_page as usize]).is_some()
        {
            earliest_use[*future_page as usize] = Some(index);
            needed_pages.insert(*future_page);
        }
    }

    // currently active pages in memory
    let unused_active_pages: Vec<FrameNumber> = page_table
        .iter()
        .filter(|x| x.is_some())
        .map(|x| x.unwrap())
        // filter out any that will be used in the future
        .filter(|x| !needed_pages.contains(x))
        .collect();

    if unused_active_pages.len() > 0 {
        return unused_active_pages[0];
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

    #[test]
    fn test_find_last_use_never_used() {
        let future_pages = vec![3, 0, 4, 2, 3, 0, 3, 2, 3];
        // let page_table: Vec<Option<u32>> = vec![Some(7), Some(0), Some(1), Some(2)];
        let page_table: Vec<Option<u32>> =
            vec![Some(1), Some(2), Some(3), None, None, None, None, Some(0)];
        assert_eq!(find_last_use(&future_pages, &page_table), 7);
    }
}
