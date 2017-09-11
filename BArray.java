//The code was used as sample code by the lecturer of data structure. I would like to use this snippet to demonstrate my code style.

/*
 * An expandable array with basic operations like get, set, add, delete, sort, search
 */

//Both used for random shuffling
import java.util.Arrays;
import java.util.Collections;

public class BArray<T extends Comparable<T>> {
	private int numOfElement;
	private int k;
	private int numOfBranch;
	private T[][] catalogArray;
	
	public BArray() {
		numOfElement = 0;
		k = 1;
		numOfBranch = 1;
		catalogArray = (T[][])new Comparable[2][2];
		catalogArray[1] = null;
	}
	
	public BArray(int n) {
		numOfElement = 0;
		k = (int) (Math.ceil(Math.log(n)/Math.log(2))/2);
		numOfBranch = 1;
		catalogArray = (T[][])new Comparable[(1<<k)][(1<<k)];
		for (int i=1;i<(1<<k);i++) {
			catalogArray[i] = null;
		}
	}
	
	private void largerArray() {
		k++;
		numOfBranch/=2;
		T[][] newArray = (T[][])new Comparable[(1<<k)][(1<<k)];
		int i=0;
		for (;i<numOfBranch;i++) {
			newArray[i] = (T[])new Comparable[numOfBranch*4];
			for (int j=0;j<numOfBranch*4;j++) {
				newArray[i][j] =
					catalogArray[i*2 + j/(numOfBranch*2)][j%(numOfBranch*2)];
			}
		}
		for (;i<numOfBranch*4;i++) {
			newArray[i] = null;
		}
		catalogArray = newArray;
	}
	
	private void smallerArray() {
		k--;
		numOfBranch*=2;
		T[][] newArray = (T[][])new Comparable[(1<<k)][(1<<k)];
		for (int i = 0; i<numOfBranch; i++) {
			newArray[i] = (T[])new Comparable[numOfBranch];
			for (int j=0; j<numOfBranch; j++) {
				newArray[i][j] = catalogArray[i/2][j + (i%2)*numOfBranch];
			}
		}
		catalogArray = newArray;
	}
	
	private void addBranch() {
		catalogArray[(++numOfBranch - 1)] = (T[])new Comparable[(1<<k)];
	}
	
	private void deleteBranch() {
		catalogArray[--numOfBranch] = null;
	}
	
	private static<E extends Comparable<E>> void swap(E[][] a, int i, int j, int p, int q) {
		E temp=a[i][j];
		a[i][j]=a[p][q];
		a[p][q]=temp;
	}
	
	private int intoPlace(T[][] a, int low, int high) {
		int i=low, j=high+1;
		T key=get(low);
		while (true) {
			while (get(++i).compareTo(key)<0) 
				if (i == high) break;
			while (get(--j).compareTo(key)>0) 
				if (j == low) break;
			if (i >= j) break;
			swap(a, i/(1<<k), i%(1<<k), j/(1<<k), j%(1<<k));
		}
		swap(a,low/(1<<k), low%(1<<k), j/(1<<k), j%(1<<k));
		return j;
	}
	
	private void quickSort(T[][] a, int low, int high) {
		if (high <= low) return;
		int key = intoPlace(a,low,high);
		quickSort(a,low,key-1);
		quickSort(a,key+1,high);
	}
	
	public void quickSort() {
		for (int i=0;i<numOfBranch - 1;i++) {
			Collections.shuffle(Arrays.asList(catalogArray[i]));
		}
		Collections.shuffle(Arrays.asList(Arrays.copyOfRange(catalogArray[numOfBranch - 1], 0, (numOfElement - 1)%(1<<k))));
		quickSort(catalogArray, 0, numOfElement - 1);
	}
	
	public void add(T item, int i) {
		if ((1<<(2*k)) == numOfElement) {
			largerArray();
			addBranch();
		}
		else if (0 == numOfElement%((1<<k)) && numOfElement != 0) {
			addBranch();
		}
		numOfElement++;
		for (int j = i + 1; j<numOfElement; j++) {
			set(get(j - 1), j);
		}
		set(item, i);
	}
	
	public void delete(int i) {
		if (i>numOfElement) {
			throw new ArrayIndexOutOfBoundsException();
		}
		for (int j = i; j<numOfElement - 1; j++) {
			set(get(j + 1), j);
		}
		numOfElement--;
		if (1<<(2*(k-1)) == numOfElement) {
			deleteBranch();
			smallerArray();
		} else if (0 == numOfElement%((1<<k))) {
			deleteBranch();
		}
	}
	
	public int search(T item) {
		for (int i=1; i<=numOfElement; i++) {
			if (get(i).equals(item)) return i;
		}
		return -1;
	}
	
	public T get(int i) {
		if (i>numOfElement) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return catalogArray[(i)/(1<<k)][(i) % (1<<k)];
	}
	
	public void set(T item, int i) {
		if (i>numOfElement) {
			throw new ArrayIndexOutOfBoundsException();
		}
		catalogArray[(i)/(1<<k)][(i) % (1<<k)]=item;
	}
	
	public int length() {
		return numOfElement;
	}
	
	public String toString() {
		String result = new String();
		for (int i=0;i<numOfBranch - 1;i++) {
			for (int j=0;j<catalogArray[i].length;j++) {
				result += (catalogArray[i][j] + "\t");
			}
			result += "\n";
		}
		for (int i=0;i<=(numOfElement - 1) % (1<<k);i++) {
			result += (catalogArray[numOfBranch - 1][i] + "\t");
		}
		return result;
	}
}
