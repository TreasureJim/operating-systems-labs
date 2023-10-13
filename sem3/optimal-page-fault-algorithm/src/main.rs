const ADDRESS_FILE_PATH: &str = "../resources/addresses.txt";

mod memorymanager;

fn main() {
    let addresses_str = std::fs::read_to_string(ADDRESS_FILE_PATH).unwrap();
    let addresses: Vec<u32> = addresses_str
        .lines()
        .map(|x| x.parse::<u32>().unwrap())
        .collect();

    let mut manager = memorymanager::MemoryManager::new(&addresses);

    for addr in addresses {
        manager.read_memory(addr);
    }

    println!("Total page faults: {}", manager.num_page_faults);
}
