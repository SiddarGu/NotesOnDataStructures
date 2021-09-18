import java.util.ArrayList;

/** AAXTree (skeleton)
 *
 * 
 */

public class AAXTree<Key extends Comparable<Key>, Value> {

	private abstract class Node{
		public abstract Value find(Key x);
		public abstract Node insert(Key x, Value v) throws Exception;
		public abstract int getLevel();
		public abstract String toString();
		public abstract Node delete(Key x) throws Exception;
		public abstract Node skew();
		public abstract Node split();
		public abstract Node getLeft();
		public abstract Node getRight();
		public abstract void setLeft(Node v);
		public abstract void setRight(Node v);
		public abstract void setLevel(int i);
		public abstract Value getV();
		public abstract Key getK();
	}

	private class InNode extends Node{
		Key k;
		Node left, right;
		int lv;

		public Key getK() {
			return k;
		}

		public Value getV() {
			return null;
		}

		public Node getLeft() {
			return left;
		}

		public void setLevel(int i) {
			lv = i;
		}

		public Node getRight() {
			return right;
		}

		public void setLeft(Node v) {
			left = v;
		}

		public void setRight(Node v) {
			right = v;
		}

		public InNode(Key k, int lv) {
			this.k = k;
			this.lv = lv;
			left = right = null;
		}

		public Value find(Key x) {
			if (x.compareTo(k) < 0) {
				return left.find(x);
			} else {
				return right.find(x);
			}
		}

		public Node insert(Key x, Value v) throws Exception {
			if (x.compareTo(k) < 0) {
				left = left.insert(x, v);
			} else {
				right = right.insert(x, v);
			}
			return this.skew().split();
		}

		public Node skew() {
			
			if (this.left.getLevel() == lv) {
				Node q = getLeft();
				this.left = q.getRight();
				q.setRight(this);
				if (root == this) {
					root = q;
				}
				return q;
			} else {
				return this;
			}
		}

		public Node split() {
			if (this.right.getRight().getLevel() == this.lv) {
				Node q = this.right;
				setRight(q.getLeft());
				q.setLeft(this);
				q.setLevel(q.getLevel() + 1);
				
				if (root == this) {
					root = q;
				}
				return q;
			} else {
				return this;
			}
			
		}

		public int getLevel() {
			return lv;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("(");
			return sb.append(k + ") " + lv).toString();
		}

		public Node delete(Key x) throws Exception {
			if (x.compareTo(k) < 0) {
				Node del = left.delete(x);
				
				if (del == null) {
					if (root == this) {
						root = right;
					}
					return right;
				} else {
					left = del;
					return fixAfterDelete();
				}
			} else {
				Node del = right.delete(x);

				if (del == null) {
					if (root == this) {
						root = left;
					}
					return left;
				} else {
					right = del;
					return fixAfterDelete();
				}
			}
		}

		public void updateLevel() {
			int newLv = 1 + (left.getLevel() > right.getLevel() ? right.getLevel() : left.getLevel());
			if (lv > newLv) {
				lv = newLv;
				if (right.getLevel() > newLv) {
					right.setLevel(newLv);
				}
			}
		}

		public Node fixAfterDelete() {
			updateLevel();
			Node newThis = this.skew();
			Node r = newThis.getRight();
			r = r.skew();
			r.setRight(r.getRight().skew());
			newThis.setRight(r);
			newThis = newThis.split();
			r = newThis.getRight();
			r = r.split();
			newThis.setRight(r);
			return newThis;
		}

	}

	private class ExNode extends Node{
		Key k;
		Value v;
		final static int lv = 0;

		public Key getK() {
			return k;
		}

		public Node getLeft() {
			return this;
		}

		public Node getRight() {
			return this;
		}

		public void setLeft(Node v) {
		}

		public Node skew() {
			return this;
		}

		public Node split(){
			return this;
		}

		public void setRight(Node v) {
		}

		public void setLevel(int i) {
		}

		public ExNode(Key k, Value v) {
			this.k = k;
			this.v = v;
		}

		public Value find(Key x) {
			if (x.compareTo(k) == 0) {
				return v;
			} else {
				return null;
			}
		}

		public Node insert(Key x, Value v) throws Exception {
			if (x.compareTo(k) == 0) {
				throw new Exception("Insertion of duplicate key");
				
			} else {
				ExNode newNode = new ExNode(x, v);

				if (x.compareTo(k) < 0) {
					InNode i = new InNode(k, 1);
					i.setLeft(newNode);
					i.setRight(this);
					return i;
				} else {
					InNode i = new InNode(x, 1);
					i.setLeft(this);
					i.setRight(newNode);
					return i;
				}
			}
		}

		public int getLevel() {
			return lv;
		}

		public Value getV() {
			return v;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("[");
			return sb.append(k + " " + v + "]").toString();
		}

		public Node delete(Key x) throws Exception {
			if (k.compareTo(x) != 0) {
				throw new Exception("Deletion of nonexistent key");
			} else {
				if (root == this) {
					root = null;
				}
				return null;
			}
		}
	}

	private Node root;

	public AAXTree() {
		root = null;
	}
	public Value find(Key k) {
		if (root == null) {
			return null;
		} else {
			return root.find(k);
		}
	}
	public void insert(Key x, Value v) throws Exception {
		if (root == null) {
			ExNode a = new ExNode(x, v);
			root = a;
		} else {
			root = root.insert(x, v);
		}
	}

	public void clear() {
		root = null;
	}
	public ArrayList<String> getPreorderList() {
		ArrayList<String> list = new ArrayList<>();
		if (root != null) {
			aux(list, root);
		}
		
		return list;
	}

	private void aux(ArrayList<String> a, Node n) {
		a.add(n.toString());
		if (n.getLevel() > 0) {
			aux(a, n.getLeft());
			aux(a, n.getRight());
		}
	}

	public void delete(Key x) throws Exception { 
		if (root == null) {
			throw new Exception("Deletion of nonexistent key");
		} else {
			root.delete(x);
		}
	}
	public int size() { 
		if (root == null) {
			return 0;
		} else {
			return sizeHelper(root);
		}
	}

	private int sizeHelper(Node n) {
		if (n.getLevel() == 0) {
			return 1;
		} else {
			return (sizeHelper(n.getLeft()) + sizeHelper(n.getRight()));
		}
	}

	public Value getMin() {
		if (root == null) {
			return null;
		} else {
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getLeft();
			}
			return curr.getV();
		}
	}

	public Value getMax() {
		if (root == null) {
			return null;
		} else {
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getRight();
			}
			return curr.getV();
		}
	}
	public Value findSmaller(Key x) {
		if (root == null) {
			return null;
		} else {
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getLeft();
			}
			if (curr.getK().compareTo(x) >= 0) {
				return null;
			} else {
				return smallerAux(x, root);
			}			
		}
	}

	private Value smallerAux(Key x, Node head) {
		if (head.getLevel() == 0) {
			return head.getK().compareTo(x) < 0 ? head.getV() : null;
		} else {
			//Value result;
			if (head.getK().compareTo(x) >= 0) {
				return smallerAux(x, head.getLeft());
				
			} else {
				return smallerAux(x, head.getRight());
			}
		}
	}

	public Value findLarger(Key x) {
		if (root == null) {
			return null;
		} else {
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getRight();
			}
			if (curr.getK().compareTo(x) <= 0) {
				return null;
			} else {
				return largerAux(x, root);
			}
		}
	}

	private Value largerAux(Key x, Node head) {
		if (head.getLevel() == 0) {
			System.out.println("Node " + head.getK().toString());
			return head.getK().compareTo(x) > 0 ? head.getV() : null;
		} else {
			if (head.getK().compareTo(x) > 0) {
				// penis
				Value result =  largerAux(x, head.getLeft());
				if (result == null) {
					return largerAux(x, head.getRight());
				} else {
					return result;
				}
			} else {
				return largerAux(x, head.getRight());
			}
		}
	}

	public Value removeMin() {
		if (root == null) {
			return null;
		} else {
			Value v = getMin();
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getLeft();
			}
			try {
				delete(curr.getK());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return v;
		}	
	}

	public Value removeMax() {
		if (root == null) {
			return null;
		} else {
			Value v = getMax();
			Node curr = root;
			while (curr.getLevel() > 0) {
				curr = curr.getRight();
			}
		
			try {
				delete(curr.getK());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return v;
		}
	}

	public void replace(Key k, Value v) throws Exception {
		if (root == null) {
			throw new Exception("Replacement of nonexistent key");
		} else if (root.find(k) == null) {
			throw new Exception("Replacement of nonexistent key");
		} else {
			root.delete(k);
			root.insert(k, v);
		}
	}

}
