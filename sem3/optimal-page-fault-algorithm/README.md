# Best page fault algorithm

This is a implementation of the most effective page algorithm (assuming you have a list of all the pages you will need) to learn more about this algorithm and how it works see [here](https://www.scaler.com/topics/optimal-page-replacement-algorithm/).

This implementation is in rust and can use a custom list of addresses. The implementation right now is hard coded to have 256 pages in the page table. 

# How to use

Compile and run the program using the rust toolchain `cargo`. Run using command `cargo run ./example-addresses-path.txt`. 

An example addresses file is provided [here](../resources/addresses.txt).
