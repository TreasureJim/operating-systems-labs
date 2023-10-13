const ADDRESS_FILE_PATH: &str = "../resources/addresses.txt";

mod memorymanager;

fn main() {
    let addresses_str = std::fs::read_to_string(ADDRESS_FILE_PATH).unwrap();
    let addresses: Vec<u32> = addresses_str
        .lines()
        .map(|x| x.parse::<u32>().unwrap())
        .collect();

    let manager = memorymanager::MemoryManager::new(addresses);
}
