package ehaminian.gmail.com.stream1;

 public  class  Martixops {
    public static double[][] invert(double a[][]) 

    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) {
            b[i][i] = 1;
            }
        gaussian(a, index);
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]-= a[index[j]][i]*b[index[i]][k];

        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
    
    
    
    
    public static double[] subtractArrays(double[] a1,double[] a2)
    {
    	int s1=a1.length;
    	int s2=a2.length;
    	if(s1!=s2)
    	{
    		System.out.println("Array size should be the same.");
    		System.exit(1);
    	}
    	double[] res=new double[s1];
    	for(int i=0;i<s1;i++)
    	{
    		res[i]=a1[i]-a2[i];
    	}
    	return res;
    }
    
    public static double[][] makeMatrix(double[] _array)
    {
    	int s=_array.length;
    	double[][] m=new double[1][s];
    	for(int i=0;i<s;i++)
    		m[0][i]=_array[i];
    	return m;
    }
    
    public static double[][] transposeArray(double[] _array){
    	double[][] m=new double[_array.length][1];
    	for(int i=0;i<_array.length;i++)
    		m[i][0]=_array[i];
        return m;
    }
    
    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    
    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
    	int r1=firstMatrix.length;
    	int c1=firstMatrix[0].length;
    	int r2=secondMatrix.length;
    	int c2=secondMatrix[0].length;
    	if(c1!=r2)
    	{
    		System.out.println("Matrix size mismatch");
    		System.out.println("The first matrix size is: "+r1+" * "+c1);
    		System.out.println("The second matrix size is: "+r2+" * "+c2);
    		System.exit(1);
    	}
    	double[][] product = new double[r1][c2];
        for(int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return product;
    }
    
    
    public static void gaussian(double a[][], int index[]) 
    {
        int n = index.length;
        double c[] = new double[n];
        // Initialize the index
        for (int i=0; i<n; ++i) 
            index[i] = i;
        for (int i=0; i<n; ++i) 
        {
            double c1 = 0;
            for (int j=0; j<n; ++j) 
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
                double pj = a[index[i]][j]/a[index[j]][j];
                a[index[i]][j] = pj;
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
}
