# NotesOnDataStructures
## BST
#### Each node in the BST contains a key-value pair and pointers to its children. The advantage of a BST is that find(), insert(), and delete() are in O(log n) (Array sorted by key requires O(n)). Notice that an unbalanced BST can behave like a sequential structure in the worst case. The height of the BST can vary from O(log n) to O(n).<br /> Java implementation:<br /> - Parameterize key-value types: extends Comparable<br /> - The root node is private<br /> - All operations run in O(height) time.<br />
## AVL Tree
#### The height of an AVL tree with n nodes is guaranteed to be in O(log n). After each insertion, check the height of the left and right subtrees. The tree will be rebalanced if the heights differ by at least 2. The AVL tree uses left and right rotations to maintain the balanced structure. The AVL tree then updates the height of each node accordingly.
## AA Tree
## K-D Tree
