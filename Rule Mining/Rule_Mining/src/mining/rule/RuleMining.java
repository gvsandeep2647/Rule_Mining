package mining.rule;

import java.io.*;
import java.util.*;

import org.omg.CORBA.TRANSACTION_MODE;

/**
 * @author Sandeep,Snehal,Poojitha
 *	AIM : To Use hash tree and come up with interesting association rules by using apriori algorithm.
 */
public class RuleMining {
	/** An ArrayList of Integer Arrays which store the stands of a candidate in a binary format*/
	public static ArrayList<int[]> data = new ArrayList<int[]>(); 
	public static ArrayList<ArrayList<Set<Integer>>> itemsets = new ArrayList<ArrayList<Set<Integer>>>();
	//public static ArrayList<ArrayList<Integer>> sub = new ArrayList<ArrayList<Integer>>();

	public static void main(String args[]){
		try{
			inputHandle("dataFile.txt",data);
		}catch(Exception e){
			System.out.println("Error In file Reading " + e);
		}

		Scanner in = new Scanner(System.in);
		System.out.println("Enter The Value for Minimum Support:\n");
		double minSupport = in.nextDouble();
		System.out.println("Enter The value for Minimum Confidence:\n");
		double minConfidence = in.nextDouble();

		DataRef dref = new DataRef();

		/** Contains all the sets of one Frequent Items based on the given minimum Support Value*/
		ArrayList<Set<Integer>> oneFreq = oneFrequentItemSet(data,dref,minSupport);
		itemsets.add(0,oneFreq);

		ArrayList<ArrayList<Integer>> refined = attributeRepresentation(data);

		RuleMining ref = new RuleMining();
		/** One transaction taken here for example **/

		//		ref.tranBreakdown(refined.get(0),3);
		//		System.out.println("Transaction: "+refined.get(0));
		//		System.out.println(sub.size());
		//		for(int i =0;i<sub.size();i++){
		//			System.out.println(sub.get(i));
		//		}

		kminus1tok(ref,oneFreq,1);

		//		HashTree root = new HashTree(3,0);
		//		TreeSet<Integer> test = new TreeSet<Integer>();
		//		test.add(1);
		//		test.add(4);
		//		test.add(7);
		//		boolean status = root.hashItemset(test);
		//		test.clear();
		//		test.add(1);
		//		test.add(4);
		//		test.add(10);
		//		status = root.hashItemset(test);
		//		test.clear();
		//		test.add(1);
		//		test.add(4);
		//		test.add(13);
		//		status = root.hashItemset(test);
		//		test.clear();
		//		test.add(1);
		//		test.add(4);
		//		test.add(16);
		//		status = root.hashItemset(test);

		in.close();
	}

	/**
	 * @param filename : the filename to be opened
	 * @param data : The ArrayList of data objects which is to be populated on the basis of data
	 * @throws IOException : To handle File not found error.
	 * @see IOException
	 */
	public static void inputHandle(String filename,ArrayList<int[]> data)throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line=null;
		while( (line=br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line,",");
			int row[] = new int[17];
			while(st.hasMoreTokens()){
				for(int i=0;i<17;i++)
				{
					row[i] = getInt(st.nextToken());
				}
			}

			data.add(expand(row));
		}       
		br.close();
	}

	/**
	 * @param dataField The string from the input. Can be a 'y','n','republican','democrat', or ?
	 * @return An integer corresponding to the the input. y : 1 \n n : 1\n republican: 1\n democrat: 0\n ?: -1 
	 */
	public static int getInt(String dataField){
		if(dataField.equals("y"))
			return 1;
		else if(dataField.equals("n"))
			return 0;
		else if(dataField.equals("?"))
			return -1;
		else if(dataField.equals("republican"))
			return 1;
		else 
			return 0;
	}

	/**
	 * @param row the row which we created after reading in the data
	 * @return a row with binarized categorical data. The attribute corresponding to the data can be found in DataRef Class
	 */
	public static int[] expand(int row[]){
		int ans[] = new int[34];
		int j = 0;
		for(int i=0;i<row.length;i++)
		{
			if(row[i]==1)
			{
				ans[j]   = 0;
				ans[j+1] = 1;
			}else if(row[i]==0){
				ans[j]   = 1;
				ans[j+1] = 0;
			}
			j = j+2;
		}
		return ans;
	}

	/**
	 * @param row the row which we want to print
	 * An utility function that prints the passed array
	 */
	public static void printArray(int []row){
		for(int i=0;i<row.length;i++)
			System.out.print(row[i]+" ");
		System.out.println();
	}
	public static void printArray(double []row){
		for(int i=0;i<row.length;i++)
			System.out.print(row[i]+" ");
		System.out.println();
	}

	/**
	 * @param data The data in the binary format
	 * @param dref The class which stores the text corresponding to the the index value
	 * @param minSupport The Support Threshold
	 * @return The ArrayList which contains all sets of 1-frequent items corresponding to the minimum Support Threshold
	 */
	public static ArrayList<Set<Integer>> oneFrequentItemSet(ArrayList<int []>data,DataRef dref,double minSupport){
		ArrayList<Set<Integer>> frequent = new ArrayList<Set<Integer>>();
		int vectorLen = data.get(0).length;
		double supportValues[] = new double[vectorLen];
		for(int i = 0; i < data.size();i++)
		{
			for(int j = 0;j<vectorLen;j++)
			{
				if(data.get(i)[j]==1)
					supportValues[j]++;
			}
		}

		for(int i=0; i< supportValues.length;i++)
			supportValues[i] = supportValues[i]/data.size();

		for(int i=0;i< supportValues.length;i++)
		{
			if(supportValues[i]>=minSupport)
			{
				Set<Integer> temp = new TreeSet<Integer>();
				temp.add(i);
				frequent.add(temp);
			}
		}
		return frequent;
	}

	/**
	 * @param data The data in the binary format
	 * @return The set representation of the voting patterns.
	 */
	public static ArrayList<ArrayList<Integer>> attributeRepresentation(ArrayList<int[]> data){
		ArrayList<ArrayList<Integer>> data_rept = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<data.size();i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int j=0;j<data.get(0).length;j++)
			{
				if(data.get(i)[j]==1)					
					temp.add(j);

			}
			data_rept.add(temp);
		}

		return data_rept;
	}


	/**
	 * @param sets All K-1 dimension sets
	 * @return All Possible K dimension sets obtained after having cross product : K-1 X K-1
	 */
	public static void kminus1tok(RuleMining ref,ArrayList<Set<Integer>> sets,int k){

		ArrayList<Set<Integer>> temp = new ArrayList<Set<Integer>>();
		TreeSet<Integer> candidate = new TreeSet<Integer>();
		TreeSet<Integer> toMerge = new TreeSet<Integer>();
		for(int i=0;i<sets.size();i++)
		{
			for(int j=i+1;j<sets.size();j++)
			{
				candidate = new TreeSet<Integer>();
				candidate.addAll(sets.get(i));
				int lastI = candidate.last();
				candidate.remove(lastI);
				toMerge.addAll(sets.get(j));
				int lastJ = toMerge.last();
				toMerge.remove(lastJ);
				candidate.addAll(toMerge);
				if(candidate.size()==toMerge.size())
				{
					candidate.add(lastI);
					candidate.add(lastJ);
					temp.add(candidate);
				}
			}		
		}
		
		itemsets.add(k,prepruning(ref,temp,k));
	}
	
	public static ArrayList<Set<Integer>> prepruning(RuleMining ref,ArrayList<Set<Integer>> kfrequent,int k){
		ArrayList<Set<Integer>> finalKfrequent = new ArrayList<Set<Integer>>();
		ArrayList<Set<Integer>> kminus1 = itemsets.get(k-1);
		for(int i=0;i<kfrequent.size();i++)
		{
			int tem[] = toInt(kfrequent.get(i));
			int flag = 1;
			for(int j=0;j<tem.length-1;j++)
			{
				if(tem[j]%2==0 && tem[j+1] == tem[j]+1)
				{
					flag = 0;
					break;
				}
			}
			
			if(flag==1)
			{
				ArrayList<Integer> al_kfruent = new ArrayList<Integer>();
				for (int index = 0; index < tem.length; index++)
				    al_kfruent.add(tem[index]);
				ArrayList<ArrayList<Integer>> kminus1cand = ref.tranBreakdown(al_kfruent,k-1);
				for(int p=0;p<kminus1cand.size();p++)
				{
					System.out.println(kminus1cand.get(p));
					TreeSet<Integer> tempSet = new TreeSet<>(kminus1cand.get(p));
					if(!kminus1.contains(tempSet))
					{
						flag = 0;
						break;
					}
				}
			}
			if(flag==1)
			{
				TreeSet<Integer> toBeConsidered = new TreeSet<>();
				for(int s=0;s<tem.length;s++)
					toBeConsidered.add(tem[s]);
				finalKfrequent.add(toBeConsidered);
			}
		}
		return finalKfrequent;
	}
	
	public static int[] toInt(Set<Integer> set) {
		  int[] a = new int[set.size()];
		  int i = 0;
		  for (Integer val : set) a[i++] = val;
		  return a;
	}
	
	public  ArrayList<ArrayList<Integer>> tranBreakdown (ArrayList<Integer> transaction, int k){
		ArrayList<ArrayList<Integer>> sub = new ArrayList<ArrayList<Integer>>();
		int len = transaction.size();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		this.breakdown(sub,transaction,temp,0,len-1,0,k);
		return sub;
	}

	public void breakdown(ArrayList<ArrayList<Integer>> sub,ArrayList<Integer> trans, ArrayList<Integer> temp, int low, int high, int point, int k){
		if(point==k){
			ArrayList<Integer> copy = new ArrayList<Integer>();
			for(int i = 0;i<temp.size();i++){
				copy.add(temp.get(i));
			}
			sub.add(copy);
			return;
		}	
		for(int i=low; (i <= high) && (high-i+1 >= k-point); i++ ){
			temp.add(point,trans.get(i));
			this.breakdown(sub,trans,temp,i+1,high,point+1,k);
			temp.remove(point);

		}
	}
}		