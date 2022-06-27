# codeThesis

As of this year we need to include a brief description of the programming files used to construct this thesis. All code is commented, however this will help you understand the structure of the code. All my code is in Java and I used eclipse to program and run my code. I did not need data or any additional libraries, which could complicate the set up. So I hope there are no set up complications. 

I first did the replication part. My results can be re-obtained by running the method `createAllTable()' in Tables.java or by running the methods in this file separately. For the replication part I used the file ServiceLevel.java to calculate the service levels and Simulation.java for the optimization of $S$ and $S_c$ (simulation.java is not a very good name for this class). To keep the code relatively organised, I used F.java for simple functions I use through out other classes. I used Function.java to calculate the integrals needed for the approximation of the critical service level.

For the extension I use TablesExtension.java to create the tables. To obtain the values in these tables I use the methods in Extension.java. This class contains both the methods for optimising $S$ and $S_c$ and for calculating the service levels, in the extended setting. For the extension I use ItegralsExtension.java to calculate the integrals for the approximation in the extended setting.

I hope this helps to better understand my code.
