import java.util.ArrayList;

public class Point2D {
	final static int DIM = 2; // spatial dimension
	double[] coord; // coordinates

	public Point2D() {
		coord = new double[DIM];
		for (int i = 0; i < DIM; i++)
			coord[i] = 0;
	}

	public Point2D(double x, double y) {
		coord = new double[DIM];
		coord[0] = x;
		coord[1] = y;
	}

	public Point2D(double[] coord) {
		assert (coord.length == DIM);
		this.coord = new double[DIM];
		for (int i = 0; i < DIM; i++)
			this.coord[i] = coord[i];
	}

	public Point2D(Point2D p) {
		coord = new double[DIM];
		for (int i = 0; i < DIM; i++)
			coord[i] = p.get(i);
	}

	public static int getDim() {
		return DIM;
	}

	public double get(int i) {
		return coord[i];
	}

	public void set(int i, double x) {
		coord[i] = x;
	}

	public void setLocation(double x, double y) {
		coord[0] = x;
		coord[1] = y;
	}

	public double getX() {
		return coord[0];
	}

	public double getY() {
		return coord[1];
	}

	public boolean equals(Point2D pt) {
		for (int i = 0; i < DIM; i++) {
			if (pt.coord[i] != coord[i]) return false;
		}
		return true;
	}

	public double distance(Point2D pt) {
		double sum = 0;
		for (int i = 0; i < DIM; i++) {
			sum += Math.pow(pt.coord[i] - coord[i], 2);
		}
		return (double) Math.sqrt(sum);
	}

	public double distanceSq(Point2D pt) {
		double sum = 0;
		for (int i = 0; i < DIM; i++) {
			sum += Math.pow(pt.coord[i] - coord[i], 2);
		}
		return sum;
	}

	public String toString() {
		return "(" + coord[0] + "," + coord[1] + ")";
	}
}


/**
 * WKDTree
 *
 */

public class WKDTree<Point2D> {

	// -----------------------------------------------------------------
	// Public members - You should not modify the function signatures
	//
	// We will make our canonical solution available for these. You are
	// free to use ours or your own.
	// -----------------------------------------------------------------

	private abstract class Node {
		public abstract Point2D find(Point2D pt);
		public abstract Node insert(Point2D pt) throws Exception;
		public abstract Node delete(Point2D pt) throws Exception;
		public abstract boolean isExternal();
		public abstract Node getLeft();
		public abstract Node getRight();
		public abstract String toString();
		public abstract Point2D getMinMax(int dim, int comp);
		public abstract Point2D findAux(int dim, int comp, double f);
		public abstract Point2D getPt();
		public abstract Rectangle2D getWrapper();
	}

	private class inNode extends Node {
		int cutDim;
		double cutVal;
		Rectangle2D wrapper;
		Node left, right;

		public inNode(int cutDim, double cutVal) {
			this.cutDim = cutDim;
			this.cutVal = cutVal;
			left = right = null;
		}

		public Point2D find(Point2D pt) {
			if (pt.coord[cutDim] < cutVal) {
				return left.find(pt);
			} else {
				return right.find(pt);
			}
		}

		public Node insert(Point2D pt) throws Exception {
			if (pt.get(cutDim) < cutVal) {
				left = left.insert(pt);
			} else {
				right = right.insert(pt);
			}
			// update wrapper
			wrapper.add(pt);
			return this;
		}

		public Node delete(Point2D pt) throws Exception {
			if (pt.get(cutDim) < cutVal) {
				Node del = left.delete(pt);

				if (del == null) {
					if (root == this) {
						root = right;
					}
					return right;
				} else {
					left = del;
					// update wrapper
					wrapper = Rectangle2D.union(left.getWrapper(), right.getWrapper());
					return this;
				}
			} else {
				Node del = right.delete(pt);

				if (del == null) {
					if (root == this) {
						root = left;
					}
					return left;
				} else {
					right = del;
					// update wrapper
					wrapper = Rectangle2D.union(left.getWrapper(), right.getWrapper());
					return this;
				}
			}
		}

		public void setWrapper(Rectangle2D wrapper) {
			this.wrapper = wrapper;
		}

		public void setLeft(Node n) {
			left = n;
		}

		public void setRight(Node n) {
			right = n;
		}

		public boolean isExternal() {
			return false;
		}

		public Rectangle2D getWrapper() {
			return wrapper;
		}

		public Node getLeft() {
			return left;
		}

		public Node getRight() {
			return right;
		}

		public Point2D getPt() {
			return null;
		}

		public String toString() {
			StringBuilder s = new StringBuilder("(");
			String axis = cutDim == 0 ? "x=" : "y=";
			s.append(axis + cutVal + "): ");
			s.append(wrapper.toString());
			return s.toString();
		}

		public Point2D getMinMax(int dim, int comp) {
			Point2D l = left.getMinMax(dim, comp);
			Point2D r = right.getMinMax(dim, comp);
			if (cutDim == dim) {
				return comp < 0 ? l : r;
			} else { 
				if (comp == -1) { // get min
					if (r.get(dim) > l.get(dim)) {
						return l;
					} else if (r.get(dim) < l.get(dim)) {
						return r;
					} else {
						return l.get(1-dim) > r.get(1-dim) ? r : l;
					}
				} else {
					if (r.get(dim) > l.get(dim)) {
						return r;
					} else if (r.get(dim) < l.get(dim)) {
						return l;
					} else {
						return l.get(1-dim) > r.get(1-dim) ? l : r;
					}
				}
			}
		}

		public Point2D findAux(int dim, int comp, double f) {
			if (comp > 0) { // find larger
				if (wrapper.getHigh().get(dim) < f) {
					return null;
				} else {
					if (left.findAux(dim, comp, f) == null) {
						return right.findAux(dim, comp, f);
					} else if (right.findAux(dim, comp, f) == null) {
						return left.findAux(dim, comp, f);
					} else {
						Point2D l = left.findAux(dim, comp, f);
						Point2D r = right.findAux(dim, comp, f);
						
						if (l.get(dim) < r.get(dim)) {
							return l;
						} else if (l.get(dim) > r.get(dim)) {
							return r;
						} else {
							return l.get(1-dim) < r.get(1-dim) ? l : r;
						}
					}
				}
			} else {
				if (wrapper.getLow().get(dim) > f) {
					return null;
				} else {
					if (left.findAux(dim, comp, f) == null) {
						return right.findAux(dim, comp, f);
					} else if (right.findAux(dim, comp, f) == null) {
						return left.findAux(dim, comp, f);
					} else {
						Point2D l = left.findAux(dim, comp, f);
						Point2D r = right.findAux(dim, comp, f);

						if (l.get(dim) < r.get(dim)) {
							return r;
						} else if (l.get(dim) > r.get(dim)) {
							return l;
						} else {
							return l.get(1-dim) < r.get(1-dim) ? r : l;
						}
					}
				}
			}
		}
	}

	private class exNode extends Node {
		Point2D thisPt;

		public exNode(Point2D pt) {
			thisPt = pt;
		}

		public Node getLeft() {
			return this;
		}

		public Node getRight() {
			return this;
		}

		public Point2D find(Point2D pt) {
			if (pt.equals(thisPt)) {
				return thisPt;
			} else {
				return null;
			}
		}

		public Node insert(Point2D pt) throws Exception {
			if (pt.equals(thisPt)) {
				throw new Exception("Insertion of point with duplicate coordinates");
			} else {
				exNode newEx = new exNode(pt);
				Rectangle2D newRec = new Rectangle2D(pt, thisPt);
				int wider = newRec.getWidth(0) < newRec.getWidth(1) ? 1 : 0;
				inNode newIn = new inNode(wider, newRec.getCenter().get(wider));
				newIn.setWrapper(newRec);

				if (pt.get(wider) < newRec.getCenter().get(wider)) {
					newIn.setLeft(newEx);
					newIn.setRight(this);
				} else {
					newIn.setLeft(this);
					newIn.setRight(newEx);
				}

				if (root == this) {
					root = newIn;
				}
				return newIn;
			}
		}

		public Node delete(Point2D pt) throws Exception {
			if (pt.getX() != thisPt.getX() || pt.getY() != thisPt.getY()) {
				throw new Exception("Deletion of nonexistent point");
			} else {
				if (root == this) {
					root = null;
				}
				return null;
			}
		}

		public Rectangle2D getWrapper() {
			return new Rectangle2D(thisPt, thisPt);
		}

		public boolean isExternal() {
			return true;
		}

		public Point2D getPt() {
			return thisPt;
		}

		public String toString() {
			StringBuilder s = new StringBuilder("[");
			s.append(thisPt.toString());
			return s.append("]").toString();
		}

		public Point2D getMinMax(int dim, int comp) {
			return thisPt;
		}

		public Point2D findAux(int dim, int comp, double f) {
			if (comp > 0) {
				return thisPt.get(dim) > f ? thisPt : null;
			} else {
				return thisPt.get(dim) < f ? thisPt : null;
			}
		}

	}

	private Node root;

	public WKDTree() { 
		root = null;	
	}
	public Point2D find(Point2D pt) {
		if (root == null) {
			return null;
		} else {
			return root.find(pt);
		}
	}
	public void insert(Point2D pt) throws Exception {
		if (root == null) {
			root = new exNode(pt);
		} else {
			root.insert(pt);
		}
	}
	public void delete(Point2D pt) throws Exception {
		if (root == null) {
			throw new Exception("Deletion of nonexistent point");
		} else {
			root.delete(pt);
		}
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
		if (!n.isExternal()) {
			aux(a, n.getLeft());
			aux(a, n.getRight());
		}
	}

	public void clear() {
		root = null;
	}
	public int size() {
		if (root == null) {
			return 0;
		} else {
			return sizeHelper(root);
		}
	}

	private int sizeHelper(Node n) {
		if (n.isExternal()) {
			return 1;
		} else {
			return (sizeHelper(n.getLeft()) + sizeHelper(n.getRight()));
		}
	}

	public Point2D getMinX() {
		if (root == null) {
			return null;
		} else {
			return root.getMinMax(0, -1);
		}
	}
	public Point2D getMaxX() {
		if (root == null) {
			return null;
		} else {
			return root.getMinMax(0, 1);
		}
	}
	public Point2D getMinY() {
		if (root == null) {
			return null;
		} else {
			return root.getMinMax(1, -1);
		}
	}
	public Point2D getMaxY() {
		if (root == null) {
			return null;
		} else {
			return root.getMinMax(1, 1);
		}
	}
	public Point2D findSmallerX(double x) {
		if (root == null || getMinX().getX() > x) {
			return null;
		} else {
			return root.findAux(0, -1, x);
		}
	}
	public Point2D findLargerX(double x)  {
		if (root == null || getMaxX().getX() < x) {
			return null;
		} else {
			return root.findAux(0, 1, x);
		}
	}
	public Point2D findSmallerY(double y) {
		if (root == null || getMinY().getY() > y) {
			return null;
		} else {
			return root.findAux(1, -1, y);
		}
	}
	public Point2D findLargerY(double y)  {
		if (root == null || getMaxY().getY() < y) {
			return null;
		} else {
			return root.findAux(1, 1, y);
		}
	}
	public ArrayList<Point2D> circularRange(Point2D center, double sqRadius) {
		ArrayList<Point2D> lst = new ArrayList<>();

		if (root != null) {
			auxRange(lst, root, center, sqRadius);
		}

		return lst;
	}

	public void auxRange(ArrayList<Point2D> a, Node n, Point2D center, double sqRadius) {
		if (n.isExternal()) {
			if (n.getPt().distanceSq(center) <= sqRadius) {
				a.add(n.getPt());
			}
		} else {
			auxRange(a, n.getLeft(), center, sqRadius);
			auxRange(a, n.getRight(), center, sqRadius);
		}
	}

	// -----------------------------------------------------------------
	// New for Programming Assignment 3 - Fixed-radius nearest neighbor
	// -----------------------------------------------------------------
	
	public Point2D fixedRadNN(Point2D center, double sqRadius) {
		ArrayList<Point2D> pts = circularRange(center, sqRadius);

		// remove the curr pt
		for (int i = 0; i < pts.size(); i++) {
			Point2D curr = pts.get(i);
			if (curr.equals(center)) {
				pts.remove(i);
				break;
			}
		}

		if (pts.size() == 0) {
			return null;
		} else {
			Point2D min = pts.get(0);

			for (int i = 1; i < pts.size(); i++) {
				Point2D curr = pts.get(i);

				if (curr.distanceSq(center) < min.distanceSq(center)) {
					min = curr;
				} else if (curr.distanceSq(center) == min.distanceSq(center)) {
					if (curr.getX() < min.getX()) {
						min = curr;
					} else if (curr.getX() == min.getX()) {
						if (curr.getY() < min.getY()) {
							min = curr;
						}
					}
				}
			}
			return min;
		}
	}
}
