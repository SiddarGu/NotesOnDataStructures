# NotesOnDataStructures
## BST
#### Each node in the BST contains a key-value pair and pointers to its children. The advantage of a BST is that find(), insert(), and delete() are in O(log n) (Array sorted by key requires O(n)). Notice that an unbalanced BST can behave like a sequential structure in the worst case. The height of the BST can vary from O(log n) to O(n).<br /> Java implementation:<br /> - Parameterize key-value types: extends Comparable<br /> - The root node is private<br /> - All operations run in O(height) time.
