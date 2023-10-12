const ADDRESS_FILE_PATH: &str = "../resources/addresses.txt";
const PAGE_SIZE: usize = 256;

fn main() {
    let addresses_str = std::fs::read_to_string(ADDRESS_FILE_PATH).unwrap();
    let addresses: Vec<&str> = addresses_str.lines().collect();

    let manager = MemoryManager::new();
}

trait MemoryManager {}

struct MemoryManager {
    page_table: [Option<u16>; PAGE_SIZE],
}

impl MemoryManager {
    pub fn new() -> Self {
        Self {
            page_table: [None; PAGE_SIZE],
        }
    }
}
