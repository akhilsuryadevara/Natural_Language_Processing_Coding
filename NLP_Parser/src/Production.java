/**This implements a production in a pcfg grammar
 *
 * @author Paul Chase: chaspau@iit.edu
 * @version 1.0
 * 
 */

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class Production
{
	double probability;
	double logProb;
	String left;
	String right[];
	int dot;
	int start;
	//CS585_HW2:: this is a link to backtrack the production and
	//recreate the parse tree
	ArrayList<Production> backtrack;

	//Production does double duty as a parse tree; this is for that.
	//therefore, has same number of children as right[], one for
	//each; if there is no child there, null is stored instead.
	Production children[];
	//parent is for the linking as well.
	Production parent;

	/**Simple constructor, assumes no children, initializes everybody.*/
	Production()
	{
		probability=0.0;
		logProb=0.0;
		left = "";
		right = null;
		dot = 0;
		start = 0;
		children = null;
		parent = null;
		
		//CS585_HW2:: this is a link to backtrack the rules that lead to 
		//the current production to recreate the parse tree 
		backtrack=new ArrayList<Production>();
		
	}

	/**Constructs a production with n right productions.*/
	Production(int n)
	{
		this();
		right = new String[n];
		children = new Production[n];
		for(int i=0;i<n;i++)
		{
			right[i] = null;
			children[i] = null;
		}
	}

	/**Constructs a production with the given right hand side.*/
	Production(String[] rhs)
	{
		this(rhs.length);
		right = rhs;
	}

	/**Copy constructor.*/
	Production(Production p)
	{
		probability = p.probability;
		logProb=p.logProb;
		left = p.left;
		right = p.right;
		dot = p.dot;
		start = p.start;
		children = p.children;
		//CS585_HW2: We initialize the backtrack link
		backtrack= (ArrayList<Production>) p.backtrack.clone(); 
		
	}

	/**This creates a child of the production given its index.
	 * This adds the child to the production and sets the parent for
	 * the newly created child production.
	 * 
	 * @param n the index on the right hand side where the child attaches
	 * @return The newly created child
	 */
	public final Production spawn(int n)
	{
		Production p = new Production();
		p.parent = this;
		children[n] = p;
		return p;
	}

	/**This creates a child of the production given its index.
         * This adds the child to the production and sets the parent for
         * the newly created child production.  The new child production
	 * will be a copy of the production input as a parameter.
         *
         * @param n the index on the right hand side where the child attaches
	 * @param prod the production to copy the child from
         * @return The newly created child
         */
        public final Production spawn(int n, Production prod)
        {
                Production p = new Production(prod);
                p.parent = this;
                children[n] = p;
                return p;
        }
	
	/**This returns true if the given production matches this one.
	 *
	 * The comparison checks for identical productions only, down to the
	 * placement of the dot.
	 * 
	 * @param p The production to compare to.
	 */
	public final boolean equals(Production p)
	{
		if(left != p.left || right.length != p.right.length || dot != p.dot || start != p.start)
			return false;
		for(int i=0;i<right.length;i++)
			if(right[i] != p.right[i])
				return false;
		return true;
	}

	/**Easy print.
	 */
	public void print()
	{
		System.out.println(this.toString());
	}

	/**Standard toString human-readable output.
	 * Format:
	 * startpos  left-- right1 . right2
	 * with the dot moving about accordingly.
	 */
	public String toString()
	{
		String ret = start+"\t"+left+"->";
                for(int i=0;i<right.length;i++)
                {
                        if(i==dot)
                                ret = ret + "\t.";
                        ret = ret + "\t" + right[i];
                }
                if(dot == right.length)
                        ret = ret + "\t.";
                return ret;
	}

	/**This prints a parse, a chain of productions.
	 * TODO: Write this function!
	 */
	//CS585_HW2: this function prints out the rules used to
	//successfully parse the sentence. This is useful
	//for diagnosis and clear visualization of the parsing process
	public void recursivePrint()
	{	
		if(this.backtrack!=null) {

			//System.out.println(this);
			Iterator<Production> iter = backtrack.iterator();
			while(iter.hasNext()) {
				iter.next().recursivePrint();

			}

		}
		System.out.println(this);
		
	}
	
	//CS585_HW2: this function prints the parsed sentence in
	//form of a bracketed tree as requested in the assignment
	public void bracketedForm()
	{
		int count = 0;
		if(this.left.charAt(0) != 'f')
		{
			System.out.print(this.left);
			System.out.print("[ ");
			count++;
		}
		else
		{
			System.out.print(this.left);
			System.out.print("[ ");
			System.out.print(this.right[0]);

		}
			
		if(backtrack!=null) {

			Iterator<Production> iter = backtrack.iterator();
			while(iter.hasNext()) {
				iter.next().bracketedForm();
				System.out.print("] ");
			}

		}
	}
	
	
//	public double totalProb()
//	{
//		double ruleProb= this.probability;
//		if(this.backtrack!=null) {
//			for (Production rule : backtrack){
//				ruleProb=ruleProb*rule.probability;
//			}
//		}
//		return ruleProb;
//	}
}
