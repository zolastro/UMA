import java.util.*;

public class MergeSort {

	private static final int SIZE = 100000;

	public static void main(String[] args) throws InterruptedException {
		List<Integer> numbers = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < SIZE; i++) {
			numbers.add(rand.nextInt(SIZE));
		}
		System.out.println(numbers);
		Node a = new Node(numbers);
		a.start();
		a.join();
		System.out.println(numbers);
	}

}

class Node extends Thread {

	private List<Integer> list;

	public Node(List<Integer> l) {
		list = l;
		// System.out.println("Node created!\n" + list);
	}

	private void add(List<Integer> l, int from, int to) {
		l.addAll(list.subList(from, to));
	}

	private void merge(List<Integer> la, List<Integer> lb) {
		int i = 0;
		int j = 0;
		int k = 0;
		list.clear();

		// System.out.println("Mergin : " + la + " & " + lb);
		while (i < la.size() && j < lb.size()) {
			if (la.get(i) < lb.get(j)) {
				list.add(k, la.get(i));
				i++;
			} else {
				list.add(k, lb.get(j));
				j++;
			}
			k++;
		}
		if (i < la.size()) {
			list.addAll(la.subList(i, la.size()));
		}
		if (j < lb.size()) {
			list.addAll(lb.subList(j, lb.size()));
		}
		// System.out.println("Merged!\n" + list);

	}

	public void run() {
		if (list.size() < 2) {
			return;
		} else {
			List<Integer> l1 = new ArrayList<>();
			List<Integer> l2 = new ArrayList<>();
			add(l1, 0, list.size() / 2);
			add(l2, list.size() / 2, list.size());
			Node n1 = new Node(l1);
			Node n2 = new Node(l2);
			n1.start();
			n2.start();
			try {
				n1.join();
				n2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			merge(l1, l2);
		}
	}

	private void waitNodes(Node n1, Node n2) {

	}
}
