/** 
 * Implementation of Two choice Hashing.
 *
 * @author Rammurthy Mudimadugula (rxm163730)
 * @author Soorya Prasanna Ravichandran (sxr152130)
 * @author Nagasupreeth Ramesh (nxr150830)
 */
import java.util.*;

@SuppressWarnings("unchecked")
class TwoChoice<T> {
	int N;
    int size;
    int prime;
    List<T>[] H;
    int maxListSize;
    int emptyLists;

    TwoChoice(int n) {
    	N = n;
    	H = new List[N];
    	size = 0;
    	maxListSize = 0;
    	emptyLists = n;
        prime = getPrime(N);
    	
        for (int i = 0; i < N; i++) {
            H[i] = new LinkedList<T>();
        }
    }

    private int h1(T x) {
        int hashVal = (x.hashCode()*x.hashCode()*37+(37*x.hashCode())+37);
        hashVal = hashVal % N;
        if (hashVal < 0) {
            hashVal = hashVal + N;
        }
        return hashVal;
    }

    private int h2(T x) {
        int hashVal = (x.hashCode()*x.hashCode()*37+(37*x.hashCode())+37);
        hashVal = hashVal % N;
        if (hashVal < 0) {
            hashVal = hashVal + N;
        }
        return prime - hashVal % prime;
    }

    public boolean add(T x) {
    	if (contains(x)) {
    		return false;
    	} else {
    		int i1 = h1(x);
    		int i2 = h2(x);

    		if (H[i1].size() < H[i2].size()) {
    			if (H[i1].size() == 0) {
    				emptyLists--;
    			}
    			H[i1].add(x);
    			if (H[i2].size() > maxListSize) {
    				maxListSize = H[i2].size();
    			}
    		} else {
    			if (H[i2].size() == 0) {
    				emptyLists--;
    			}
    			H[i2].add(x);
    			if (H[i1].size() > maxListSize) {
    				maxListSize = H[i1].size();
    			}
    		}
    		size++;
    		return true;
    	}
    }

    public boolean contains(T x) {
    	int i1 = h1(x);
    	int i2 = h2(x);

    	return H[i1].contains(x) || H[i2].contains(x);
    }

    public void remove (T x) {
    	int i1 = h1(x);
    	int i2 = h2(x);

    	if (H[i1].remove(x) || H[i2].remove(x)) {
    		size--;
    	}
    }

    private static int randomNumberGenerator(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max-min) + min;
        return randomNum;
    }

    private static int getPrime(int n) {
        int num = randomNumberGenerator(2, n) + 1;

        while (!isPrime(num)) {          
            num = randomNumberGenerator(2, n) + 1;
        }
        return num;
    }

    private static boolean isPrime(int n) {
        if( n == 2 || n == 3 ) {
            return true;
        }

        if( n == 1 || n % 2 == 0 ) {
            return false;
        }

        for( int i = 3; i * i <= n; i += 2 ) {
            if( n % i == 0 ) {
                return false;
            }
        }

        return true;
    }

    public static void main (String[] args) {
    	if (args.length == 2) {
    		int n = Integer.parseInt(args[1])*1000000;
    		int ts = Integer.parseInt(args[0]);
    		TwoChoice<Integer> tch = new TwoChoice<>(ts);

			for (int i = 0; i < n; i++) {
				tch.add(randomNumberGenerator(0, n*10));
			}
			System.out.println("Maximum size of the list: " + tch.maxListSize);
			System.out.println("Number of empty lists: " + tch.emptyLists);
    	} else {
    		System.out.println("USAGE: java TwoChoice <table_size> <number_of_elements_to_insert>");
    		System.out.println("Example: java TwoChoice 10000 1");
    		System.out.println("Note: Number of elements to insert digit is automatically converted to millions");
    	}
    }
}