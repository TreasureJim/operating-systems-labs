use std::env;

mod memorymanager;

fn main() {
    let args: Vec<String> = env::args().collect();
    let address_str = args.get(1).expect("No path for addresses provided");

    let addresses_str = std::fs::read_to_string(address_str).unwrap();
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
