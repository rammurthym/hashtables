/** 
 * Implementation of HopScotch Hashing.
 *
 * @author Rammurthy Mudimadugula (rxm163730)
 * @author Soorya Prasanna Ravichandran (sxr152130)
 * @author Nagasupreeth Ramesh (nxr150830)
 */

import java.util.Set;
import java.util.*;

@SuppressWarnings("unchecked")
public class HashTable<T> {
    static int initialSize = 16;
    static int k = 3;
    static int resizeCalled = 0;

    class HashEntry<E> {
        E element;
        boolean occupied;
        HashEntry(E x) {
            element = x;
            occupied = true;
        }
    }

    HashEntry<T>[] table;
    int size, maxSize;
    Set<T> overflow;
    int prime = 13;

    HashTable() {
        table = new HashEntry[initialSize];
        size = 0; 
        maxSize = initialSize;
        overflow = new HashSet<>();
        for (int i = 0; i < maxSize; i++) {
            table[i] = new HashEntry(null);
            table[i].occupied = false;
        }
    }

    HashTable(int n) {
        initialSize = n;
        table = new HashEntry[initialSize];
        size = 0; 
        maxSize = initialSize;
        overflow = new HashSet<>();
        for (int i = 0; i < maxSize; i++) {
            table[i] = new HashEntry(null);
            table[i].occupied = false;
        }
    }

    private int myhash1(T x) {
        int hashVal = (x.hashCode()*x.hashCode()*37+(37*x.hashCode())+37);
        hashVal = hashVal % maxSize;
        if (hashVal < 0) {
            hashVal = hashVal + maxSize;
        }
        return hashVal;
    }

    private int myhash2(T x) {
        int hashVal = (x.hashCode()*x.hashCode()*37+(37*x.hashCode())+37);
        hashVal = hashVal % maxSize;
        if (hashVal < 0) {
            hashVal = hashVal + maxSize;
        }
        return prime - hashVal % prime;
    }


    int find(T x) {
        int s0 = myhash1(x);

        if (overflow.contains(x)) {
            return -1;
        }

        if (!table[s0].occupied || table[s0].element == x) {
            return s0;
        } else {
            int si;
            for (int i = 1; i <= k; i++) {
                si = (s0 + myhash2(x)*i) % maxSize;
                if (!table[si].occupied || table[si].element == x) {
                    return si;
                } else if (i == k) {
                    return si;
                }
            }
        }
        return 0;
    }

    private int add(T x, int i) {
        if (table[i].occupied && table[i].element == x) {
            return -1;
        } else if (!table[i].occupied) {
            table[i].element = x;
            table[i].occupied = true;
            size++;
            return 0;
        } else {
            return 1;
        }
    }

    public boolean add(T x) {
        if (size() == maxSize) {
            resize();
        }
        int s0 = myhash1(x);
        int check = add(x, s0);

        if (check < 0) {
            return false;
        } else if (check == 0) {
            return true;
        } else {
            int si;
            for (int i = 1; i <= k; i++) {
                si = (s0 + myhash2(x)*i) % maxSize;
                check = add(x, si);
                if (check < 0) {
                    return false;
                } else if (check == 0) {
                    return true;
                }
            }
            overflow.add(x);
        }
        return true;
    }

    public int size() {
        return overflow.size() + size;
    }

    public T get(T x) {
        int check = find(x);
        if (check < 0) {
            return x;
        } else {
            if (table[check].element == x) {
                return x;
            } else {
                return null;
            }
        }
    }

    public T remove(T x) {
        int check = find(x);
        if (check < 0) {
            overflow.remove(x);
            return x;
        } else {
            if (table[check].element == x) {
                table[check].occupied = false;
                size--;
                return x;
            } else {
                return null;
            }
        }
    }

    public boolean contains(T x) {
        int check = find(x);
        if (check < 0) {
            return true;
        } else {
            if (table[check].element == x) {
                return true;
            } else {
                return false;
            }
        }
    }

    void resize() {
        resizeCalled++;
        HashTable<T> temp = new HashTable<>(maxSize*4);

        temp.k = k + resizeCalled;
        for (int i = 0; i < maxSize; i++) {
            if (table[i].occupied) {
                temp.add(table[i].element);
            }
        }
        for (T x: overflow) {
            temp.add(x);
        }
        table = temp.table;
        size = temp.size;
        overflow = temp.overflow;
        maxSize = temp.maxSize;
    }

    void display() {
        for (int i = 0; i < maxSize; i++) {
            if (table[i].element != null) {
                System.out.println(table[i].element);
            }
        }

        if (overflow.size() > 0) {
            for (T x: overflow) {
                System.out.println(x);
            }
        }
    }

    private static int randomNumberGenerator(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max-min) + min;
        return randomNum;
    }

    public static void main (String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[1])*1000000;
            if (args[0].equals("0")) {
                Set<Integer> hs = new HashSet<>();

                Timer t1 = new Timer();
                t1.start();
                for (int i = 0; i < n; i++) {
                    hs.add(i);
                }
                t1.end();
                System.out.println("Time taken to add " + args[1] + " million elements in Java's hash set: " + t1);

                Timer t2 = new Timer();
                t2.start();
                hs.remove(randomNumberGenerator(0, n));
                t2.end();
                System.out.println("Time taken to remove a random element from Java's hash set: " + t2);

            } else if (args[0].equals("1")) {
                HashTable<Integer> ht = new HashTable<>();
        
                Timer t1 = new Timer();
                t1.start();
                for (int i = 0; i < n; i++) {
                    ht.add(i);
                }
                t1.end();
                System.out.println("Time taken to add " + args[1] + " million elements in Double hashing table: " + t1);

                Timer t2 = new Timer();
                t2.start();
                ht.remove(randomNumberGenerator(0, n));
                t2.end();
                System.out.println("Time taken to remove a random element from Double hashing table: " + t2);

                System.out.println("Number of times table is resized: " + ht.resizeCalled);

            } else {
                System.out.println("Incorrect argument value. Please pass 0 or 1 as argument");
            }
        } else {
            System.out.println("USAGE: java HashTable 0/1");
            System.out.println("0: Java's HashTable, 1: Double Hashing");
        }
    }
}
