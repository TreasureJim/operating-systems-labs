# Task 1

You can see the reader is reading out values that aren't accurate. 
It is because multiple threads are editing the shared data simulateously without waiting for each other to exit their critical section.
You can see the starvation problem because the writer threads almost always wait until the readers finish. 

# Task 2

