import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


public class DBApp implements DBAppInterface{
	Vector <Table> tables ;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
		BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
		String current = "";
		boolean flag =false;
		if (br.readLine() == null) {
			flag =true;
		}
		br.close();
		if(flag) {
			
			FileWriter csv = new FileWriter("src/main/resources/metadata.csv",true);
			csv.append("Table Name, Column Name, Column Type, ClusteringKey, Indexed, min, max");
		    csv.append("\n");
			csv.flush();
			csv.close();
		}
		
		
		}
		catch(IOException e){
		e.printStackTrace();
		}

			
	}
	public DBApp() {
	//	this.tables = new Vector<Table>();
	}

	@Override
	public void createTable(String tableName, String clusteringKey, Hashtable<String, String> colNameType,
			Hashtable<String, String> colNameMin, Hashtable<String, String> colNameMax) throws DBAppException {
	
		try{
			if (tableExist(tableName) == true) {
				throw new DBAppException();
				}
			else {
				
				try {
					this.tables=readVectorTable();
					Table t = new Table (tableName);
					tables.add(t);
					writeTables(tables);
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}      
        Enumeration<String> values = colNameType.keys();
        ArrayList columns= new ArrayList<>();
        
        while(values.hasMoreElements() ){
       	 String data=values.nextElement().toString();
       	 columns.add(data);
       	 
           }
        
        for(int i=0;i<columns.size();i++) {

       	 String input=tableName+","+columns.get(i)+", ";
       	 input+=colNameType.get(columns.get(i))+", ";
       	 
       	 if(clusteringKey.equals(columns.get(i))) {
       		 input+="True,";
       		
       		 }
       	 else {
       		 input+="False,";
       		 }
       	 input+="False,"; // index
       	 String var="java.lang.String";
       	 
       	 String x = colNameType.get(columns.get(i));
       	 if(	 (!x.equals("java.lang.Integer")) &&
       			 (!x.equals("java.lang.String")) &&
       			 (!x.equals("java.lang.Double")) &&
       			 (!x.equals("java.util.Date"))   ){
       		 throw new DBAppException();
       	 }else {
       		
       	 }
       	 
       	 if(colNameType.get(columns.get(i)).equals(var)) {
       		 input+='"'+ colNameMin.get(columns.get(i))+ '"'+",";
           	 input+='"'+colNameMax.get(columns.get(i))+ '"';
       	 }
       	 else {
       	 input+= colNameMin.get(columns.get(i))+",";
       	 input+=colNameMax.get(columns.get(i));
       	 }
       	 writecsv(input);
		}
			}
		}
		
		
		
		catch (IOException e) {
	         e.printStackTrace();
	      }
		
	}

	public  boolean tableExist(String tableName) {

		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
			String current = "";
		
		while ((current =br.readLine())!= null) {
			String[] line = current.split(",");
			 if (line[0].equalsIgnoreCase(tableName))
		    		return true;
			
		}
		br.close();}
		catch(IOException e){
		e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void createIndex(String tableName, String[] columnNames) throws DBAppException {
		
		Grid g = new Grid(columnNames);
		try {
			this.tables=readVectorTable();
			Table t = readTables(tableName);
			try {
				String gridsName = tableName+"_grids";
				t.grids = t.readGrids(gridsName);
				g.createGrid(tableName, columnNames, t,t.grids.size());
				t.grids.add(g);
				t.writeToGrids(gridsName, t.grids);
				updateIndexedStatusCSV(columnNames , tableName);
			
				
			} catch (DBAppException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public  void checkinput(String tablename , Hashtable<String, Object> colNameValue) throws DBAppException, ParseException {
		try {
			String ClusteringKey="";
			boolean output=true;
			Enumeration<String> values1 = colNameValue.keys();
		    Enumeration<Object> values2 = colNameValue.elements();
		        ArrayList input= new ArrayList<>();
		        while(values1.hasMoreElements() ){
		            String data=values1.nextElement().toString();
		            Object data2=values2.nextElement();
		            input.add(data);
		            input.add(data2);
		        }
	        boolean ckey=false;
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
			String current = "";
			ArrayList con = new ArrayList();
			while ((current =br.readLine())!= null) {
				String[] line = current.split(",");
				for(int i=0 ;i<line.length;i+=7) {
					if(line[i].equals(tablename)) {
						ArrayList con2 = new ArrayList();
						for (int j=i;j<7;j++) {
							con.add(line[j] );
							//find the clustring key
							String x = (line[i+3].toString()).replaceAll("\\s", "");
							if(x.equalsIgnoreCase("True")) {
								ClusteringKey=line[i+1];
								}	}	}	}	}
		
		boolean fine = false;
		
		for (int i=0;i<input.size() ;i+=2) {	
			for(int j=1; j<con.size();j+=7) {
				String conS = ((String) con.get(j)).replaceAll("\\s", "");
				String inputS = ((String) input.get(i)).replaceAll("\\s", "");				
				
				if(conS.equals(inputS)){
					fine=true;
				}
				
				
		}if(!fine) {
			throw new DBAppException();
		}else {
			fine = false;
		}
		}
		
		
		
		
		
		boolean colexist=false;

			if(input.contains(ClusteringKey)) {
				ckey=true;	
				for (int i=0;i<input.size() ;i+=2) {
					
					for(int j=1; j<con.size();j+=7) {
						String con1 = ((String) con.get(j+1)).replaceAll("\\s", "");
						String conS = ((String) con.get(j)).replaceAll("\\s", "");
						String inputS = ((String) input.get(i)).replaceAll("\\s", "");
						
						if(conS.equals(inputS)){
							colexist=true;
							
							if(!chkType ( input.get(i+1) , con1 , con.get(j+4) , con.get(j+5) )){ 
								output=false;
								throw new DBAppException();

							}
							else {
						
							}	
						}
						
				}
				if(colexist==false) {//check key exists in table
					output=false;
					throw new DBAppException();
				}
				
				}
			}
		else {
			
			output=false;
			throw new DBAppException();
		}
		br.close();
		}
		catch(IOException e){
		e.printStackTrace();
		}
		
	}
	
	public  void checkinputUpdate(String tablename , Hashtable<String, Object> colNameValue) throws DBAppException, ParseException {
		try {
			String ClusteringKey="";
			boolean output=true;
			Enumeration<String> values1 = colNameValue.keys();
		    Enumeration<Object> values2 = colNameValue.elements();
		        ArrayList input= new ArrayList<>();
		        while(values1.hasMoreElements() ){
		            String data=values1.nextElement().toString();
		            Object data2=values2.nextElement();
		            input.add(data);
		            input.add(data2);
		        }
	        boolean ckey=false;
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
			String current = "";
			ArrayList con = new ArrayList();
			while ((current =br.readLine())!= null) {
				String[] line = current.split(",");
				for(int i=0 ;i<line.length;i+=7) {
					if(line[i].equals(tablename)) {
						ArrayList con2 = new ArrayList();
						for (int j=i;j<7;j++) {
							con.add(line[j] );
							//find the clustring key
							String x = (line[i+3].toString()).replaceAll("\\s", "");
							if(x.equalsIgnoreCase("True")) {
								ClusteringKey=line[i+1];
								}	}	}	}	}
		
		boolean fine = false;
		
		for (int i=0;i<input.size() ;i+=2) {	
			for(int j=1; j<con.size();j+=7) {
				String conS = ((String) con.get(j)).replaceAll("\\s", "");
				String inputS = ((String) input.get(i)).replaceAll("\\s", "");				
				
                  
				if(conS.equals(inputS)){
					fine=true;
				}
				
				
		}
			if(!fine) {
			System.out.println("Are you fine");
			throw new DBAppException();
		}else {
			fine = false;
		}
		}
		br.close();
		}
		catch(IOException e){
		e.printStackTrace();
		}
		
	}
	
	@Override
	public void insertIntoTable(String tableName, Hashtable<String, Object> colNameValue) throws DBAppException {


		if(tableExist(tableName)==false) {
			throw new DBAppException();

		}
		try {
			checkinput(tableName,colNameValue);
			this.tables=readVectorTable();
			Table t = readTables(tableName);
			//t.page=t.readTablePages(tableName);
		    t.insertInTable(tableName, colNameValue);
		    //t.writeTablePages(t.page, tableName);
		    writeTables(tables);
			    
			    
		}
//		removes failures but adds an error
		catch (DBAppException e1) {
			 e1.printStackTrace();
			throw new DBAppException();
		}
		catch (Exception e) {
	         e.printStackTrace();
	      }	
		}
	
	@Override
	public void updateTable(String tableName, String clusteringKeyValue, Hashtable<String, Object> columnNameValue)
			throws DBAppException {
		// TODO Auto-generated method stub
		if(tableExist(tableName)==false) {
			throw new DBAppException();
		} 
		try {  		       
			
			
			this.tables=readVectorTable();
		    checkinputUpdate(tableName,columnNameValue);
		    (readTables(tableName)).updateT(tableName, clusteringKeyValue , columnNameValue);
		     writeTables(tables);
		}
				//removes failures but adds an error
				catch (DBAppException e1) {
					
					System.out.println("Db exception catch");
					throw new DBAppException();
				}
		        catch(Exception e){
		        	e.printStackTrace();
					System.out.println("Exception catch");
		           // System.out.println("el table mosh mawgood");
		        }
	}
		
	@Override
	public void deleteFromTable(String tableName, Hashtable<String, Object> columnNameValue) throws DBAppException {
		// TODO Auto-generated method stub
		
		if(tableExist(tableName)==false) {
			throw new DBAppException();
		} 
		try {
		      
				
				this.tables=readVectorTable();
		        for(int i =0; i< tables.size() ; i++) {
		        	if((tables.get(i).name).equals(tableName)) {
		        		Table t =tables.get(i);
		        	}
		        
		       }
		        Table t = readTableDeletion(tableName , this.tables);
		        
		      
		        
		       
		        System.out.println("gego");
		     
		       
		        checkinputUpdate(tableName,columnNameValue);
		     
		       
		        t.deleteFromT(tableName, columnNameValue);
		       
		 
		        writeTables(tables);
		        }
				 catch(DBAppException e1){
			            
			            throw new DBAppException();
			        }
				
		        catch(Exception e){
		            System.out.println("Table does not exist");
		        }
}
		

	@Override
	public Iterator selectFromTable( SQLTerm[] sqlTerms, String[] arrayOperators ) throws DBAppException {	
	
		try {
			
			if(sqlTerms.length == 0 ) {
				throw new DBAppException();
			}
			checkTypesSql(sqlTerms);
			
			try {
				Iterator g=selectLinearSql( sqlTerms , arrayOperators );
				return g;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
			
			
		} catch (DBAppException e) {
			e.printStackTrace();
			throw new DBAppException();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static int getIteratorSize(Iterator iterator){
        AtomicInteger count = new AtomicInteger(0);
        iterator.forEachRemaining(element -> {
            count.incrementAndGet();
        });
        return count.get();
    }
	public boolean chkType(Object var,String x, Object min, Object max) throws ParseException, DBAppException{
			String type = (var.getClass()).toString();	 
	        String r="";
	        boolean range=false;
	        if(type.substring(16).equals("Integer")) {
	        	r="java.lang.Integer";
	        	if(min.equals(null)|| max.equals(null)) {
	        		range=true;
	        	}
	        	else if ( (int)var >= Integer.parseInt((String) min) && (int)var<=Integer.parseInt((String)max)) {
	        		range=true;
	        	}
	        			
	        }
	        if(type.substring(16).equals("Double")) {
	        	r="java.lang.Double";
	        	if(min.equals(null)|| max.equals(null)) {
	        		range=true;
	        	}
	        	else if ( (double)var >= Double.parseDouble((String) min) && (double)var<=Double.parseDouble((String) max)) {
	        		range=true;
	        	}
	        }
	        
	       if(type.substring(16).equals("String")) {
	    	   String f= (String) min;
	    	   String m= f.replaceAll("^\"+|\"+$", "");
	           String l= (String) max;
	           String n = l.replaceAll("^\"+|\"+$", "");
	    	   r="java.lang.String";
	    	   if(min.equals("null")|| max.equals("null")) {
	       		range=true;
	       	}
	    	   else { if ( ((String) var).compareTo((String) m) >= 0 && ((String) var).compareTo((String) n) <= 0) {
		       		range=true;
			       	}else {
			       		throw new DBAppException();
			       	}
	        }}
	     
	       if(type.substring(16).equals("Date") ){
	    	
	    	    Date dMin = new SimpleDateFormat("yyyy-MM-dd").parse((String) min);
				
				Date dMax = new SimpleDateFormat("yyyy-MM-dd").parse((String) max);
			
				if(min.equals(null)|| max.equals(null)) {
	        		range=true;
	        		}
	     		
	     		else if (((Date)var).compareTo((dMin)) >= 0 && (((Date)var).compareTo((dMax)) <= 0)) {
	           		range=true;
	           	}
	     		
	     		r="java.util.Date";
	     		}
	     	
	     	if(r.equals(x) && range) {
	     		return true;
	     	}
	     	return false;
	
	}
	
	public static int[] getconfig() {
		int maxr;
		int maxk;
		 Properties config = new Properties();
	      try {
	          InputStream input = new FileInputStream(new File("../DB2Project/src/main/resources/DBApp.config"));
	          config.load(input);
	      } catch (IOException e) {
	          e.printStackTrace();
	      }

	      maxr = Integer.parseInt(config.getProperty("MaximumRowsCountinPage"));
	      maxk = Integer.parseInt(config.getProperty("MaximumKeysCountinIndexBucket"));
	      
		int [] max= {maxr,maxk};
		return max;
	}
	
	public  void readcsv(){
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
			String current = "";
		
		while ((current =br.readLine())!= null) {
			String[] line = current.split(",");
			for(int i=0 ;i<line.length;i++) {
			}
		}
		br.close();}
		catch(IOException e){
		e.printStackTrace();
		}
		
	}
	
	public  void writecsv(String input) throws IOException {
		     	
		FileWriter csv = new FileWriter("src/main/resources/metadata.csv",true);
		csv.append(input);
	    csv.append("\n");
	

	csv.flush();
	csv.close();
}
	
	public  void writeTables( Vector  t) throws FileNotFoundException, IOException {

		String fileName = "src/main/resources/data/tables.txt";
	    try {
	    	ObjectOutputStream os =new ObjectOutputStream(new FileOutputStream(fileName) );
	    	os.writeObject(t);
	    	os.close();	
		} catch (Exception e) {
			// TODO: handle exception
		}    
  }
	
	public  Table readTables (String tableName) throws IOException, ClassNotFoundException {

		String fileName = "src/main/resources/data/tables.txt";
		Vector<Table>tt = new Vector<Table>();
		Table t;
        File file = new File(fileName);
        if (file.exists()) {
        	FileInputStream fileStream = new FileInputStream(fileName);
     	     ObjectInputStream   is = new ObjectInputStream(fileStream);
     	     tt = (Vector) is.readObject();
     	     for(int i =0;i<tt.size();i++) {
     	    	if((((Table)(tt.get(i))).name).equals(tableName)) {
     	    		t=((Table)(tt.get(i)));
     	    	   return t;
     	    	}
     	     }  
       }
        return null; 
	}
	
	public  Table readTableDeletion (String tableName , Vector vt) throws IOException, ClassNotFoundException {

		Table t;
     	     for(int i =0;i<vt.size();i++) {
     	    	if((((Table)(vt.get(i))).name).equals(tableName)) {
     	    		t=((Table)(vt.get(i)));
     	    	   return t;
     	    	}
     	     }  
        return null; 
	}
	
	public  Vector readVectorTable () throws IOException, ClassNotFoundException {
    String fileName = "src/main/resources/data/tables.txt";
		Vector<Table>tt = new Vector<Table>();
        File file = new File(fileName);
        if (file.exists()) {
        	FileInputStream fileStream = new FileInputStream(fileName);
     	     ObjectInputStream   is = new ObjectInputStream(fileStream);
     	     tt = (Vector) is.readObject();
     	    	   return tt;
     	     }  

        return tt; 
	}
		
	public static  Vector rreadPage (String tableName, int pageN) throws IOException, ClassNotFoundException {
		String fileName = "src/main/resources/data/"+tableName+"_"+pageN+".pages";
		 Vector<Tuples>tt = new Vector<Tuples>();
        File file = new File(fileName);
        if (file.exists()) {
        	FileInputStream fileStream = new FileInputStream(fileName);
     	     ObjectInputStream   is = new ObjectInputStream(fileStream);
     	     tt = (Vector) is.readObject();
            return tt;
       }
        return tt; 
	}

//check on the passed types and do not just accept any type.
	public boolean checkTypesSql( SQLTerm[] sqlTerms ) throws DBAppException, ParseException {
		String tableName = "";
		
		if(sqlTerms.length != 0 ) {
			tableName = sqlTerms[0]._strTableName;
			if( ! tableExist( tableName ) ) {
				throw new DBAppException();
			}
		}
		
		for ( int i = 0 ; i < sqlTerms.length ; i++) {
			SQLTerm term = sqlTerms[i];
			
			if( !(term._strTableName).equals(tableName) ) {
				throw new DBAppException();
			}
			
			ArrayList input= new ArrayList<>();
			input.add(term._strColumnName);
			input.add(term._objValue);
			checkInputSql( term._strTableName , input );
			
			ArrayList<String> operators = new ArrayList<String>(Arrays.asList( ">" , ">=" , "<" , "<=" , "!=" , "=" ));
			if (! operators.contains(term._strOperator)) {
				throw new DBAppException();
			} 
		}
		return true ;
	}

	public  void checkInputSql(String tablename ,  ArrayList input ) throws DBAppException, ParseException {
		try {
			boolean output=true;
			//gets columns' info.
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
			String current = "";
			ArrayList con = new ArrayList();
			while ((current =br.readLine())!= null) {
				String[] line = current.split(",");
				for(int i=0 ;i<line.length;i+=7) {
					if(line[i].equals(tablename)) {
						ArrayList con2 = new ArrayList();
						for (int j=i;j<7;j++) {
							con.add(line[j] );
								}	}	}	}
		
		boolean fine = false;
		
		//check column exists
		 for (int i=0;i<input.size() ;i+=2) {	
			for(int j=1; j<con.size();j+=7) {
				String conS = ((String) con.get(j)).replaceAll("\\s", "");
				String inputS = ((String) input.get(i)).replaceAll("\\s", "");
				String conType = ((String) con.get(j+1)).replaceAll("\\s", "");
				if(conS.equals(inputS)){
					fine=true;
					if(!chkTypeSql( input.get(i+1) , conType )) {
						fine=true;
					}
				}

		}if(!fine) {
			throw new DBAppException();
		}else {
			fine = false;
			}
		}}
		catch(IOException e){
		e.printStackTrace();
		}} 

	public boolean chkTypeSql ( Object var,String x ) throws ParseException, DBAppException{
		String type = (var.getClass()).toString();	 
	    String r="";
	    boolean range=false;
	    if(type.substring(16).equals("Integer")) {
	    	r="java.lang.Integer";    			
	    }
	    if(type.substring(16).equals("Double")) {
	    	r="java.lang.Double";
	    }
	    
	   if(type.substring(16).equals("String")) {
		    r="java.lang.String";
		  }
	 
	   if(type.substring(16).equals("Date") ){
			r="java.util.Date";
	 		}
	 	
	 	if(r.equals(x) ) {
	 		return true;
	 	}
	 	return false;

}
//return tuples passing all conditions
    public Iterator selectLinearSql(SQLTerm[] sqlTerms , String[] arrayOperators ) throws ClassNotFoundException, IOException, ParseException, DBAppException {
		
    	Object [] pagesArray = new Object [sqlTerms.length];
    	ArrayList<Tuples> outputToIterator = new ArrayList<>();
    	if(sqlTerms.length != 0 ) {
    		String tableName = sqlTerms[0]._strTableName;
    		
    		System.out.println("tablename"+tableName);
    		
    		if (!tableExist(tableName)) {
    			throw new DBAppException();
    		}
    		this.tables=readVectorTable();
			Table t = readTables(tableName);
			ArrayList<String> tableTag = t.getOrder( tableName );
			Vector pageList = t.readTablePages(tableName);
			
    		ArrayList l = new ArrayList<>();
		
    		for(int i = 0 ; i < sqlTerms.length ; i++) {
    			SQLTerm term = sqlTerms[i];
    			l.add(term._strColumnName);
    			l.add(term._strOperator);
    			l.add(term._objValue);

    		}
    		ArrayList<String>op1= new ArrayList<String>();
    		ArrayList<String>op2=new ArrayList<String>();
    		
    		boolean flag = false;
    		Vector<Grid>grids=t.readGrids(tableName);
    		Object[]a=new Object[1];
    		for (int i =0;i<l.size()-2;i=i+3) { 
    			flag=false;
    			ArrayList<String>indexcol=new ArrayList<String>();
    			indexcol.add(l.get(i).toString());
    			a[0]=l.get(i+2);
    			for (int j =0;j<grids.size();j++) {
    				if (grids.get(j).columnNames.length==1&&grids.get(j).columnNames[0].equals(l.get(i))) { ///use this grid
    					ArrayList<String> bucket=grids.get(j).getBucketDelete(indexcol, a, tableName);
    					int bucketNumber=Integer.parseInt(bucket.get(0));
    					
    					ArrayList<String>allbuckets=new ArrayList<String>();
    					
    					for (int k=0;k<10;k++) {
    						
    					switch(l.get(i+1).toString()) {
    					case ">": if (bucketNumber<=k) {
    						allbuckets.add(k+"");
    					}
    						break;
    					case ">=":if (bucketNumber<=k) {
    						allbuckets.add(k+"");
    					}
    						break;
    					case "<":if (bucketNumber>=k) {
    						allbuckets.add(k+"");
    					}
    						break;
    					case "<=":if (bucketNumber>=k) {
    						allbuckets.add(k+"");
    					}
    						break;
    					case "!=":allbuckets.add(k+"");
    						break;
    					case "=":if (k==bucketNumber)
    						allbuckets.add(bucketNumber+"");
    						break;
    					default:	
    					    break;
    					}
    					}
    					ArrayList<String> position=new ArrayList<String>();
						   
    					for (int k=0;k<allbuckets.size();k++) {
    					
    						       buckets b = new buckets();
    							   String bucketName = tableName+"_grid"+j+"_"+allbuckets.get(k);   //bucketName=tableName+"_grid"+gridN+"_"+bucketindex;
    							   Vector <pair> pair= b.readBucket(bucketName);
    							   for(int d=0;d<pair.size();d++ ) {
    									String oldpage=(pair.get(d).min).toString();
    									   if (!(position.contains(oldpage)))
    									           position.add(oldpage);
    							   }
    							 
    							   
    							   b.overflowBucket=b.readOverflowBucket(bucketName);
    							   for(int d =0;d<b.overflowBucket.size();d++) {
    							     Vector<pair> pairO=  b.readBucket(bucketName+"_overflow_"+d+"");     //bucketName+="_overflow_"+overflowBucket.size();
    								 for (int w =0;w<pairO.size();w++) {
    									String oldpage=(pairO.get(w).min).toString();
    									   if (!(position.contains(oldpage)))
    									           position.add(oldpage);
    								 }
    							   }
    							   }
    					
    					pagesArray[i/3]=position;
    					flag = true;
    				}
    			}
    			if (!flag) { ///without using index 3ade
    				String oldpage="";
    				ArrayList<String>position=new ArrayList<String>();
    	
    				for ( int w = 0 ; w  < pageList.size() ; w++ ) {
    	    			
    	    			oldpage = tableName +"_"+ w ; 
    	    			
    	    			Vector overflowList = ((pages) pageList.get(w)).readOverflowVector(tableName);
    	    		
    	    			
    	    			if (!(position.contains(oldpage)))
					           position.add(oldpage);
    	    			
    	    			for( int k = 0 ; k  < overflowList.size() ; k++ ) {
        	    			oldpage = tableName +"_"+ w+"_overflow_"+k ; 

    	    				if (!(position.contains(oldpage)))
						           position.add(oldpage);
    	    				
    	    			}
    	    			}
    				pagesArray[i/3]=position;
  				
    			}
    			
    		}
    		for (int i =0;i<pagesArray.length;i++) {
    		}
    		for (int i =0;i<pagesArray.length-1;i++) {
    			switch(arrayOperators[i]) {
    			case"AND":
    				pagesArray[i+1]= ANDList((ArrayList<String>)pagesArray[i],(ArrayList<String>)pagesArray[i+1]);
    				           break;
    			case"OR":
    				pagesArray[i+1]= ORList((ArrayList<String>)pagesArray[i],(ArrayList<String>)pagesArray[i+1]);
    				break;
    			case"XOR":
    				pagesArray[i+1]= ORList((ArrayList<String>)pagesArray[i],(ArrayList<String>)pagesArray[i+1]);
    				break;
    			default:break;
    			}
    		}
    		Vector temp=new Vector();
    		ArrayList<String>finalPages=(ArrayList<String>)(pagesArray[pagesArray.length-1]);
    		for(int i =0;i<finalPages.size();i++) {
    			temp.add(finalPages.get(i));
    		}
    		pageList=temp;
    		//loop over pages
    		for ( int i = 0 ; i  < pageList.size() ; i++ ) {
  
    			Vector<Tuples> tuples = readPage(pageList.get(i).toString());
    			for(int j = 0 ; j < tuples.size() ; j++) {
    				Tuples tuple = tuples.get(j);
    				if (linearSqlCond( tuple , tableTag , l,arrayOperators)) {
    					outputToIterator.add(tuple);
    				}
    			}
    			}
    				}
    	Iterator<Tuples> it = outputToIterator.iterator();
    	
    	return it;
    }
//ckeck if a tuple passes all conditions
    public boolean linearSqlCond( Tuples tuple , ArrayList<String> tableTag , ArrayList l,String[]operators ) {
		boolean op1 = true;
		boolean op2 = true;
		String oper = "";
		
		
		for ( int i = 0 ; i < l.size()-2 ; i+=3 ) {     ///
			String column = (String) l.get(i) ;
			String operator = (String) l.get(i+1);
			Object value = l.get(i+2);
			String type = (value.getClass()).toString();
			Object currentVal = null;
			
			for (int j = 0; j < tableTag.size(); j++) {
				if (column.equals(tableTag.get(j))) {
					currentVal = tuple.tuples[j];
				}
			}
			boolean flag = false; 
			
			switch (operator) {
			case ">":	
				    if(type.substring(16).equals("Integer")) {   			
				    	flag = (int) currentVal > (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal > (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value))  > 0;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value))  > 0;}
				break;
			case ">=":
					if(type.substring(16).equals("Integer")) {   			
						flag = (int) currentVal >= (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal >= (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value))  >= 0;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value))  >= 0;}
				break;
				
			case "<":
					if(type.substring(16).equals("Integer")) {   			
						flag = (int) currentVal < (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal < (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value))  < 0;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value))  < 0;}
				
				break;
			case "<=":
					if(type.substring(16).equals("Integer")) {   			
						flag = (int) currentVal <= (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal <= (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value)) <= 0;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value))  <= 0;}
				break;
			case "!=":
					if(type.substring(16).equals("Integer")) {   			
						flag = (int) currentVal != (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal != (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value))  != 0 ;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value))  !=  0;}
	
				break;
			case "=":
					if(type.substring(16).equals("Integer")) {   			
						flag = (int) currentVal == (int) value;}
				    if(type.substring(16).equals("Double")) {
				    	flag = (double) currentVal == (double) value;}
				    if(type.substring(16).equals("String")) {
				    	flag = (((String)currentVal).compareTo((String) value)) == 0;}
				    if(type.substring(16).equals("Date") ){
				    	flag = (((Date)currentVal).compareTo((Date) value)) == 0;}
				break;
			default:
				break;
			}
			if( i == 0 ) { 
				op1 = flag;
				oper=operators[i/3];
				
			}
			else {
				op2 = flag;
				switch (oper) {
				case "AND":
					op1 = op1 && op2 ;
					break;
				case "OR":
					op1 = op1 || op2 ;
					break;
				case "XOR":
					op1 = op1 ^ op2 ;
					break;
				default: 
					break;
				}
					
				if( i/3 <=operators.length-1) {
					oper=operators[i/3];
					
				}

				
			}			
		}
		if (op1) { // for testing nothing else
			
		}
    	return op1;
    	
    }
      
    public void updateIndexedStatusCSV( String[] columnName , String tableName  ) throws IOException, InterruptedException {
    	  
    	BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
		String current = "";
		boolean flag = false;
		ArrayList<String> finalWrite = new ArrayList<String>();
		
		while ((current =br.readLine())!= null) {
			String[] line = current.split(",");
			for(int i=0 ;i<line.length;i+=7) {
				
				if(line[i].equals(tableName)) {		
					for (int j=i;j<7;j++) {
						String x = (line[i+1].toString()).replaceAll("\\s", "");
						for( int k =0 ;k<columnName.length ; k++) {
							if(x.equalsIgnoreCase(columnName[k])) {
								line[i+4]  = "TRUE";
								flag =true;
							}
						}}
					
					if(flag) {
						finalWrite.add(line[0] + ","+ line[1] + ","+ line[2] + ","+ line[3] + ","+ line[4] + ","+ line[5] + ","+ line[6] );
					}
					else {
						finalWrite.add(current);
					}flag =false;	
				}
				else {
					finalWrite.add(current);
				}
			}}
		br.close();
				
		Path path = FileSystems.getDefault().getPath("src/main/resources/metadata.csv");
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (IOException x) {
            System.err.println(x);
        }
		
		for( int i=0 ;i< finalWrite.size() ; i++) {
			try {
				writecsv(finalWrite.get(i));
		    } catch(FileNotFoundException ex) {
		        Thread.sleep(2);
		    }
		}
    }

    public ArrayList<String> ANDList (ArrayList<String>a,ArrayList<String>b){
    	ArrayList<String>ANDList=new ArrayList<String>();
    	for (int i =0;i<a.size();i++) {
    		for (int j=0;j<b.size();j++) {
    			if (a.get(i).equals(b.get(j))) {
    				if (!(ANDList.contains(a.get(i))))
    					ANDList.add(a.get(i));
    			}
    		}
    	}
    	
    	
		return ANDList;
    	
    }
    public ArrayList<String> ORList (ArrayList<String>a,ArrayList<String>b){
    	ArrayList<String>ORList=new ArrayList<String>();
    	for(int i =0;i<a.size();i++) {
    		if (!(ORList).contains(a.get(i))) {
    			ORList.add(a.get(i));
    		}
    	}
		for(int i =0;i<b.size();i++) {
			if (!(ORList).contains(b.get(i))) {
    			ORList.add(b.get(i));
    		}
		    	}
		return ORList;
    	
    }
    public ArrayList<String> XORList (ArrayList<String>a,ArrayList<String>b){  

    	ArrayList<String>ANDList=ANDList(a,b);
    	ArrayList<String>ORList=ORList(a,b);
    	ORList.removeAll(ANDList);
		return ORList;
}
    
    

	public  Vector<Tuples> readPage (String pageName) throws IOException, ClassNotFoundException {
		String fileName = "src/main/resources/data/"+pageName+".pages";
	 	Vector<Tuples>tt = new Vector<Tuples>();
        File file = new File(fileName);
        if (file.exists()) {
        	FileInputStream fileStream = new FileInputStream(fileName);
     	     ObjectInputStream   is = new ObjectInputStream(fileStream);
     	     tt = (Vector) is.readObject();
            return tt;
       }
    return tt; 
} 
    
    
	
	public Iterator parseSQL( StringBuffer strbufSQL ) throws DBAppException, IOException, ParseException{
		System.out.println("testing comma remover::-"+ "g,e,    ,go,".replaceAll(",",""));
		String s=strbufSQL.toString();
//	    s.replaceAll(",","");
//		s.replace("(", "");
		s.replaceAll("^\"+|\"+$", "");
		String[]x=s.split(" ");
		System.out.println(Arrays.toString(x));
		switch(x[0].toLowerCase()) {
		case "create": if (x[1].equalsIgnoreCase("table")) {
		   bonusCreateTable(x);
		}
		else if (x[1].equalsIgnoreCase("index")) {
			bonusCreateIndex(x);
		}
		break;
		case "insert": bonusInsertTable(x);break;
		case "update":bonusUpdateTable(x);break;
		case "select":bonusSelectTable(x);break;
		case "delete":bonusDeleteTable(x);break;
		default:throw new DBAppException();
		}
	   return null;
		
	}			////(String tableName, String clusteringKey, Hashtable<String, String> colNameType,
	///                   Hashtable<String, String> colNameMin, Hashtable<String, String> colNameMax)
	
	//////// BONUSSSSS METHODS
	
	public Iterator bonusCreateTable(String[]x) {//[CREATE, TABLE, MyGuests, (, id, INT(6), UNSIGNED, AUTO_INCREMENT, PRIMARY, KEY,, firstname, VARCHAR(30), NOT, NULL,, lastname, VARCHAR(30), NOT, NULL,, email, VARCHAR(50),, reg_date, TIMESTAMP, DEFAULT, CURRENT_TIMESTAMP, ON, UPDATE, CURRENT_TIMESTAMP]

		String tableName = x[2];
		String clusteringKey="";
		Hashtable<String, String> colNameType= new Hashtable<String,String>();
		Hashtable<String, String> colNameMin= new Hashtable<String,String>();
		Hashtable<String, String> colNameMax= new Hashtable<String,String>();
		String dataType="";
		int haga=0;
		int i;
		if (!(x[3].equals("("))) {
			haga=1;
		}
		for (i=(4-haga);i<x.length-1;i=i+2) {  //[id, int, primary, key, firstname, varchar,primary , key];
			
			
			String col=x[i].replace("(", "");
			
			if (col.equalsIgnoreCase("CONSTRAINT")) {
				
			}
			else {
			String Type=x[i+1];
			
			switch(Type.toLowerCase().replaceAll(",","")) {

			case "varchar":
				dataType="java.lang.String";
				colNameType.put(col,dataType);
			break;
			case "char": 
				dataType="java.lang.String";
				colNameType.put(col,dataType);
				break;
			case "int": 
				dataType="java.lang.Integer";
				colNameType.put(col,dataType);
				break;
			case "float": 
				dataType="java.lang.Double";
				colNameType.put(col,dataType);
				break;
			case "date":  
				dataType="java.util.Date";
				colNameType.put(col,dataType);
				break;
			default: System.out.println("in default bonusCreate");break;
			}
			
			if ((i+2)<x.length-1 &&x[i+2].equalsIgnoreCase("primary")) {
				clusteringKey=col;
				i+=2;
			}
		}}
		
		System.out.println("clustering key :::-  "+clusteringKey+"      ");
		
		return null;
	}
	public Iterator bonusCreateIndex(String[]x) throws DBAppException {
		String indexName=x[2];
		String tableName=x[4];
		String[]col;
		ArrayList<String>s=new ArrayList<String>();
		for (int i =5;i<x.length;i++) {
			if (x[i].equals("("))
				continue;
			if (x[i].equals(")"))
				continue;
			s.add(x[i].replaceAll(",","").replace("(", "").replace(")", "").replace("(", "").replace(";", ""));
		}
		col=new String[s.size()];
		for (int i =0;i<s.size();i++) {
			col[i]=s.get(i);
		}
		//col=(String[]) s.toArray();
		System.out.println(Arrays.toString(col));
		
		createIndex(tableName,col);
		return null;
	}

    public Iterator bonusUpdateTable(String[]x) throws ParseException, IOException, DBAppException {//[UPDATE, Customers, SET, ContactName, =, 'Alfred, Schmidt',, City=, 'Frankfurt', WHERE, CustomerID, =, 1;]
		
		String tableName = x[1];
		String clusteringKey="";
		String val="";
		String col = "";
        String datatype="";
		Hashtable<String, Object> columnNameValue= new Hashtable<String,Object>();
		
		
			col=x[3];
			
			for(int i=4;i<x.length-1;i++) {
				
				if((x[i].equals("="))) {
					col = x[i-1];
					
					char c =x[i+1].charAt(0);
					String s = Character.toString(c);
					if( s.equals("'")) {
						val= x[i+1].replaceAll(",","").trim().replaceAll("'","") +" ";
						s =(x[i+1].replaceAll(",","").trim());
						c=s.charAt(s.length() - 1);
						s = Character.toString(c);
						if((s.equals("'"))) {
							
						}
						else {
							for(int j=i+2;j<x.length;j++) {
								s =(x[j].replaceAll(",","").trim());
								c=s.charAt(s.length() - 1);
								s = Character.toString(c);
								if(!((s.equals("'")))){
									val= val + x[j].replaceAll(",","").replaceAll("'","")+ " ";
								}
								else {
									val= val + x[j].replaceAll(",","").replaceAll("'","") + " ";
									columnNameValue.put(col, val);
									break;
								}

							}
						}

					}
					
				else {
					val=x[i+1].replaceAll(",","").replaceAll("'","");
					}
				}

			
//			UPDATE students\SET last_name = 'loujaina'
//			SET gpa = 0.7, last_name = 'loujaina'
			datatype=getType(tableName,col);
			System.out.println(datatype);
			switch(datatype) {
			case "java.lang.Integer" :
				int ckInt = Integer.parseInt(val);
				columnNameValue.put(col, ckInt);
				break;
			case "java.lang.Double"  : 
				double ckDoub = Double.parseDouble(val);
				columnNameValue.put(col, ckDoub);
				break;
				
			case "java.lang.String" :
				columnNameValue.put(col, val);
				break;
			
			case "java.util.Date" :	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date ckDate = sdf.parse(val);
				columnNameValue.put(col, ckDate);
				break;
			default: 	
				break;	
			}

			
			if((x[i].equalsIgnoreCase("where"))) {
				
				clusteringKey = x[i+3].replaceAll(",","").replaceAll(";","").replaceAll("'","").trim();
			}
		
			System.out.println(tableName+ " tablename");
			System.out.println(clusteringKey+ " clusteringKey");
			System.out.println(columnNameValue+ " --------> hash");
		}

			updateTable(tableName,clusteringKey,columnNameValue);
		

		return null;
	}
	public Iterator bonusInsertTable(String[]x) throws DBAppException, IOException, ParseException {
		int count=0;
		int max=0;
		String tableName=x[2];
		Hashtable<String,Object>col=new Hashtable<String,Object>();
		boolean flag=false;
		ArrayList<String>columnNames=new ArrayList<String>();
		ArrayList<Object>values=new ArrayList<Object>();
		
		for (int i =3;i<x.length;i++) {
			if (x[i].equals("("))
				continue;
			if (x[i].equals(")"))
				continue;
			if (x[i].equals(";"))
				continue;
			
			String cols =x[i].replaceAll(",","").replace("(", "").replace(")", "").replace(";", "");

			if (!flag) {
				
				columnNames.add(cols);
          }
			
			
		  if (cols.equalsIgnoreCase("Values")) {
			  flag=true;
			  
		  }
		  if (flag&&!cols.equalsIgnoreCase("Values")) {
				String val =x[i].replaceAll(",","").replace("(", "").replace(")", "").replace(";", "");
				System.out.println(val);
				System.out.println("get type output"+getType(tableName,columnNames.get(count)));
			  switch(getType(tableName,columnNames.get(count))) {
			  
				case "java.lang.String":
					values.add(val);
					break;
				case "java.lang.Integer":
					values.add(Integer.parseInt(val));
					break;
				case "java.lang.Double":
					values.add(Double.parseDouble(val));
					break;
				case "java.util.Date":
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date dDate = sdf.parse(val);
					values.add(dDate);
					break;
				default:break;
				}
			  count++;
		  }
		}
		
		for (int i =0 ;i<values.size();i++) {
			col.put(columnNames.get(i), values.get(i));
		}
		insertIntoTable(tableName,col);
		
		System.out.println(col);
		return null;
	}
	public Iterator bonusDeleteTable(String[]x) throws DBAppException{
		
		// DELETE FROM Customers WHERE CustomerName='Futterkiste' AND Country = 'Mexico' ;
		
		ArrayList<String> y = new ArrayList<String>();
		for(int i = 0 ; i<x.length ; i++){
			if( !(x[i].equals("=")) && !(x[i].equalsIgnoreCase("AND")) && !(x[i].equalsIgnoreCase(";"))){
				if( x[i].contains("=")){
					String [] xx = x[i].split("=");
					y.add(xx[0].replaceAll("'", "").replaceAll(";", ""));
					y.add(xx[1].replaceAll("'", "").replaceAll(";", ""));
				}
				else{
					y.add(x[i].replaceAll("'", "").replaceAll(";", ""));
			}	}
		}
		String[] z = new String[y.size()];
		for (int i = 0; i < y.size(); i++) {
			z[i] = y.get(i);
		}
		String tableName=z[2];;
		Hashtable<String , Object >s=new Hashtable<String , Object>();
		for (int i =4 ; i<z.length-1 ; i=i+2 ) {
			if (z[i].equals("("))
				continue;
			if (z[i].equals(")"))
				continue;
			if (z[i].equals(";"))
				continue;
			if (z[i].equals("=") || z[i].equalsIgnoreCase("AND") ){
				continue;
		   }
			String colName = z[i].replaceAll(",","").replace("(", "").replace(")", "").replace("(", "").replace(";", "").replace("=", "");
			String valString = z[i+1].replaceAll(",","").replace("(", "").replace(")", "").replace("(", "").replace(";", "").replace("'", "");
			Object colValue ; 
			try{
				colValue = Integer.parseInt(valString);
			}catch(Exception e){
				try{
					colValue = Double.parseDouble(valString); 
				}catch(Exception e1){
					try{
						colValue = Date.parse(valString);
					}catch(Exception e2){
						colValue = valString;
					}
				}
				
			}
			s.put(colName, colValue);
		}
		System.out.println(s);
		deleteFromTable(tableName,s);
		return null;
	}

	public Iterator bonusSelectTable(String[]x) {
		return null;
		}

	
	public static String getType(String tableName, String col) throws IOException {
        String r="";
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/metadata.csv"));
        String current = "";

        while ((current =br.readLine())!= null) {
            String[] line = current.split(",");

            for(int i=0 ;i<line.length;i+=7) {
                if(line[i].equals(tableName)) {

                    for (int j=i;j<7;j++) {

                        if(line[i+1].equalsIgnoreCase(col)) {

                            r=(line[i+2].toString()).replaceAll("\\s", "");;

                            return r;
                            }    }    }    }

        }

        return r;

    }
	
    @SuppressWarnings("deprecation")
	
    
    public static void main(String[] args) throws Exception {
    	DBApp dbapp=new DBApp();
    	BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
    	String x="";
    	StringBuffer s = new StringBuffer();
    	for(int i =0;i<4;i++) {
    		s.append(br.readLine());
    		s.append(" ");
    	} 
    	dbapp.parseSQL(s);
    	
    	
    	//2.5 43-8383
    	

//    	Vector<Table>tables=dbapp.readVectorTable();
//    	Table t = dbapp.readTables("students");
//    	t.page=dbapp.rreadPage("students", 0);
//    	for(int i =0;i<15;i++) {
//    		System.out.println(((Tuples)t.page.get(i)).tuples[0].toString()+" "+((Tuples)t.page.get(i)).tuples[4].toString());
//    	}
//    	
		//(String tableName, Hashtable<String, Object> columnNameValue)
    	// String tableName, String clusteringKeyValue, Hashtable<String, Object> columnNameValue)
    	 
//    	Hashtable<String,Object>h= new Hashtable<String,Object>();
//    	h.put("first_name","gego");      //1.47 43-9510
//    	h.put("gpa",new Double(0.7));
//    	dbapp.updateTable("students", "id", h);     
//    	dbapp.deleteFromTable("students", h);
//    	
//    	System.out.println("_________AFTER_________");
////
//    	for(int i =0;i<15;i++) {
//    		System.out.println(((Tuples)t.page.get(i)).tuples[0].toString()+" "+((Tuples)t.page.get(i)).tuples[4].toString());
//    	}
    	
	}
     
 

}












