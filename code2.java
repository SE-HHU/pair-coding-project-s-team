package test;
import java.util.*;
import java.io.*;

public class code2 {
		
	public static void main(String[] args) throws IOException {
		System.out.println("Please input how many questions you want"); //输入题数
		Scanner scanner = new Scanner(System.in);
		int howMany = scanner.nextInt();

		System.out.println("Please input the max natural and the max denominator you want"); //输入范围
		int maxNatural = scanner.nextInt();
		int maxDenominator = scanner.nextInt();

		System.out.println("Please input how many operators you want"); //输入符号数
		int howManyOperator = scanner.nextInt();

		System.out.println("Please tell us whether you want brackets in the questions:");//是否需要括号
		System.out.println("1.yes  0.no");
		boolean ifBracket=false;
		if (scanner.nextInt()==1)
		{
			ifBracket=true;
		}

		if(!createTxtFile("Exercises"))                           //新建题目文件
			System.out.println("The txt has already been created.");
		if(!createTxtFile("Answers"))                             //新建答案文件
			System.out.println("The txt has already been created.");

		for(int i=0;i<howMany;i++)
		{
			formula a=new formula(ifBracket,howManyOperator,maxNatural,maxDenominator);
			if(!a.ifcorrect)
			{
				i--;
				continue;
			}
			writeTxtFile(i+1+".", "Exercises");
			a.tostringquestion();
			writeTxtFile("\n","Exercises");
			writeTxtFile(i+1+".", "Answers");
			a.tostringanswer();
			writeTxtFile("\n","Answers");
		}
    }

	public static boolean createTxtFile(String name) throws IOException {
		String path=System.getProperty("user.dir");
		boolean flag = false;
		File filename = new File(path+"\\"+name+".txt");
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;
	}

	public static void writeTxtFile(String newStr,String name) throws IOException {
		try {
			String path=System.getProperty("user.dir");
			File file = new File(path+"\\"+name+".txt");
			FileWriter fw = new FileWriter(file, true);
			fw.write(newStr, 0, newStr.length());
			fw.close();
		} catch (IOException e) {
			System.out.println("序号输出异常");
		}
	}
}


class num
{
	boolean ifnatural,ifpositive;
	long n;
	long m;
	long s;//s:molecule m:denominator n:integer
	num()
	{
		ifnatural=true;
		ifpositive=true;
		n=0;
		m=0;
		s=0;
	}
	num(int a,int b)
	{
		if(b==1)
			ifpositive=true;
		else
			ifpositive=false;
		ifnatural=true;
		n=a;
		m=0;
		s=0;
	}
	num(int b,int c,int d)
	{
		if(d==1)
			ifpositive=true;
		else
			ifpositive=false;
		ifnatural=false;
		n=0;
		m=b;
		s=c;
	}
	
	void clone(num a)
	{
		if(a.ifnatural==true)
		{
			ifnatural=true;
			m=0;
			s=0;
			n=a.n;
			ifpositive=a.ifpositive;
			
		}
		else
		{
			ifnatural=false;
			m=a.m;
			n=0;
			s=a.s;
			ifpositive=a.ifpositive;
		}
	}
	
	num(boolean b,int maxnatural,int maxdenominator)//random create num
	{
		if(b==true)
		{
			ifpositive=true;
		}
		else
		{
			ifpositive=false;
		}
		if((int)(Math.random()*100+1)<24)
		{
			ifnatural=false;
			m=(int)(Math.random()*maxdenominator+2);
			s=(int)(Math.random()*m+1);
			n=0;
		}
		else
		{
			ifnatural=true;
			m=0;
			s=0;
			n=(int)(Math.random()*maxnatural+1);
		}
	}
}

class formula {
	static int getbigint(num a1)//get the integer that is bigger than number
	{
		num a = new num();
		a.clone(a1);
		int i = 0;
		while (a.s > 0) {
			a.s -= a.m;
			i++;
		}
		return i;
	}

	static int getsmallint(num a1)//get the integer that is smaller than number
	{
		num a = new num();
		a.clone(a1);
		int i = 0;
		while (a.s > a.m) {
			a.s -= a.m;
			i++;
		}
		return i;
	}

	static num sim(num a1)//Fractional simplification
	{
//		System.out.println("this is simple");
//		numprint(a1);
		if ((a1.s == 0 && a1.ifnatural == false) || (a1.n == 0 && a1.ifnatural == true)) {
			num b = new num();
			return b;
		}
		if (a1.ifnatural == true) {
			return a1;
		} else {
			num a = new num();
			a.clone(a1);
			long c;
//			System.out.println("this is for try"+a.m+"   "+a.s);
			c = getbig(a.m, a.s);
			if (c == a.m) {
				a.ifnatural = true;
				a.n = a.s / c;
			} else {
				a.s /= c;
				a.m /= c;
			}
			return a;
		}
	}

	static long getbig(long a, long b)//get Maximum common factor
	{
//		System.out.println();
//		System.out.println("get big      " + a+"   "+b);
		while (a != b) {
			if (a < b) {
				long c;
				c = a;
				a = b;
				b = c;
			}
			a -= b;

		}
		return a;
	}

	static num comden(num a1, num b1)//reduction of fractions to a common denominator
	{
		num a = new num();
		a.clone(a1);
		num b = new num();
		b.clone(b1);
		num result = new num();
		long com = 0;
		com = getbig(a.m, b.m);
		com = b.m / com;
		result = a;
		result.s *= com;
		result.m *= com;
		return result;
	}

	static num cal(num a1, num b1, int w)//a:the first number b:the second number w:which way to calculate
	{
		num a = new num();
		a.clone(a1);
		num b = new num();
		b.clone(b1);
		num result = new num();
		switch (w) {
			case 1://+
				result = add(a, b);
				break;
			case 2://-
				result = sub(a, b);
				break;
			case 3://*
				result = mul(a, b);
				break;
			case 4:///
				result = division(a, b);
				break;
			default:
				break;
		}
		return result;
	}

	static num add(num a1, num b1)//add a and b
	{
		num a = new num();
		a.clone(a1);
		num b = new num();
		b.clone(b1);
		num result = new num();
		if (a.ifpositive == b.ifpositive)//all a and b are bigger than zero
		{
			result.ifpositive = a.ifpositive;
			if (!a.ifnatural && !b.ifnatural) {
				result.ifnatural = false;
				result.m = a.m * b.m;
				result.s = (a.s * b.m + b.s * a.m);
				result = sim(result);
			} else if (a.ifnatural && b.ifnatural) {
				result.ifnatural = true;
				result.n = a.n + b.n;
			} else {
				result.ifnatural = false;
				if (!a.ifnatural) {
					num c = a;
					a = b;
					b = c;
				}
				result.m = b.m;
				result.s = b.m * a.n + b.s;
				result = sim(result);
			}
		} else//one of a and b is bigger than  zero and the other one is not
		{
			if (a.ifnatural && b.ifnatural) {
				result.ifnatural = true;
				if (a.n < b.n) {
					result.ifpositive = !a.ifpositive;
					result.n = b.n - a.n;
				} else {
					result.ifpositive = a.ifpositive;
					result.n = a.n - b.n;
				}
			} else if (!a.ifnatural && !b.ifnatural) {
				result.ifnatural = false;
				a = comden(a, b);
				b = comden(b, a);
				if (a.s < b.s) {
					result.ifpositive = !a.ifpositive;
					result.m = a.m;
					result.s = b.s - a.s;
					result = sim(result);
				} else {
					result.ifpositive = a.ifpositive;
					result.m = a.m;
					result.s = a.s - b.s;
					result = sim(result);
				}
			} else if (a.ifnatural) {
				result.ifnatural = false;
				if (a.n >= getbigint(b)) {
					result.ifpositive = a.ifpositive;
					result.s = a.n * b.m - b.s;
					result.m = b.m;
					result = sim(result);
				} else {
					result.ifpositive = !a.ifpositive;
					result.s = b.s - b.m * a.n;
					result.m = b.m;
					result = sim(result);
				}
			} else {
				result.ifnatural = false;
				if (b.n >= getbigint(a)) {
					result.ifpositive = !a.ifpositive;
					result.s = b.n * a.m - a.s;
					result.m = a.m;
					result = sim(result);
				} else {
					result.ifpositive = a.ifpositive;
					result.s = a.s - a.m * b.n;
					result.m = a.m;
					result = sim(result);
				}
			}
		}
		return result;
	}

	static num sub(num a1, num b1) {
		num a = new num();
		num b = new num();
		a.clone(a1);
		b.clone(b1);
		b.ifpositive = !b.ifpositive;
		return add(a, b);
	}

	static num mul(num a1, num b1)//multiple a and b
	{
		num a = new num();
		a.clone(a1);
		num b = new num();
		b.clone(b1);
		num result = new num();
		if (a.ifpositive == b.ifpositive) {
			result.ifpositive = true;

		} else {
			result.ifpositive = false;
		}

		if (a.ifnatural && b.ifnatural) {
			result.ifnatural = true;
			result.n = a.n * b.n;
		} else if (!a.ifnatural && !b.ifnatural) {
			result.ifnatural = false;
			result.m = a.m * b.m;
			result.s = a.s * b.s;
			result = sim(result);
		} else {
			result.ifnatural = false;
			if (!a.ifnatural) {
				result.s = a.s * b.n;
				result.m = a.m;
				result.n = 0;
				result = sim(result);
			} else {
				result.s = b.s * a.n;
				result.m = b.m;
				result.n = 0;
				result = sim(result);
			}
		}
		return result;
	}

	static num division(num a1, num b1)//a is divided by b
	{
		num a = new num();
		a.clone(a1);
		num b = new num();
		b.clone(b1);
		num result = new num();
		if (a.ifpositive == b.ifpositive) {
			result.ifpositive = true;
		} else {
			result.ifpositive = false;
		}

		if (a.ifnatural && b.ifnatural) {
			if (b.n == 0) {
				result = new num();
				return result;
			}
			result.ifnatural = false;
			result.s = a.n;
			result.m = b.n;
			result = sim(result);
		} else if (!a.ifnatural && !b.ifnatural) {
			result.ifnatural = false;
			result.s = a.s * b.m;
			result.m = a.m * b.s;
			result = sim(result);

		} else if (a.ifnatural) {
			if (a.n == 0) {
				result = new num();
				return result;
			}
			result.ifnatural = false;
			result.s = a.n * b.m;
			result.m = b.s;
			result = sim(result);
		} else {
			if (b.n == 0) {
				result = new num();
				return result;
			}
			result.ifnatural = false;
			result.s = a.s;
			result.m = a.m * b.n;
			result = sim(result);
		}
		return result;
	}

	static void numprint1(num a1) throws IOException {
		num a = new num();
		a.clone(a1);
		if (!a.ifpositive) {
			writeTxtFile("-(", "Exercises");
		}
		if (a.ifnatural) {
			writeTxtFile(String.valueOf(a.n), "Exercises");
		} else if (getbigint(a) > 1) {
			int i = getsmallint(a);
			a.s -= a.m * i;
			writeTxtFile(i + "+" + a.s + "/" + a.m, "Exercises");
		} else {
			writeTxtFile(a.s + "/" + a.m, "Exercises");
		}
		if (!a.ifpositive) {
			writeTxtFile(")", "Exercises");
		}
	}

	static void numprint2(num a1) throws IOException {
		num a = new num();
		a.clone(a1);
		if (!a.ifpositive) {
			writeTxtFile("-(", "Answers");
		}
		if (a.ifnatural) {
			writeTxtFile(String.valueOf(a.n), "Answers");
		} else if (getbigint(a) > 1) {
			int i = getsmallint(a);
			a.s -= a.m * i;
			writeTxtFile(i + "+" + a.s + "/" + a.m, "Answers");
		} else {
			writeTxtFile(a.s + "/" + a.m, "Answers");
		}
		if (!a.ifpositive) {
			writeTxtFile(")", "Answers");
		}
	}

	static boolean randboolean() {
		if ((int) (Math.random() * 20 + 1) % 2 == 1) {
			return true;
		} else {
			return false;
		}
	}

	static int randint(int a, int b) {
		if (a > b) {
			int c = a;
			a = b;
			b = c;
		}
		return (int) (Math.random() * (b - a + 1) + a);
	}

	static char operator(int a) {
		switch (a) {
			case 1:
				return '+';
			case 2:
				return '-';
			case 3:
				return '*';
			case 4:
				return '÷';
			default:
				break;
		}
		return '?';
	}

	num[] x = new num[4];
	num answer = new num();
	int order;
	int numofoperator;
	int bracket;
	int[] operator = new int[3];
	boolean ifbracket = false;
	boolean ifcorrect = true;

	/*
	 *the location of bracket
	 *
	 *
	 *
	 *
	 *
	 *
	 */
	formula() {

	}

	formula(boolean a, int howmanyoperator, int maxnatural, int maxdenominator) {
		bracket = 0;
//		System.out.println("start get the x");
		for (int i = 0; i <= howmanyoperator; i++) {
			x[i] = new num(true, maxnatural, maxdenominator);
			if (!x[i].ifnatural) {
//				numprint(x[i]);
				x[i] = sim(x[i]);
			}
//			System.out.print("x["+i+"]");
//			numprint(x[i]);
//			System.out.println();
		}
		numofoperator = howmanyoperator;
		if (a)//judge is there bracket in the formula
		{
			ifbracket = true;
		} else {
			ifbracket = false;
		}
		switch (numofoperator)//decide the order to calculate
		{
			case 1:
				order = 0;
				break;
			case 2:
				order = randint(0, 1);
				break;
			case 3:
				if (ifbracket)
					order = randint(0, 4);
				else
					order = randint(0, 3);
				break;
			default:
				break;
		}

		if (numofoperator == 1) {
			operator[0] = randint(1, 4);
			if (operator[0] == 4 && (x[1].n == 0 && x[1].ifnatural)) {
				ifcorrect = false;
			} else {
				answer = cal(x[0], x[1], operator[0]);
			}
		} else if (numofoperator == 2) {
			operator[0] = randint(1, 4);
			operator[1] = randint(1, 4);
//			System.out.println("operator[0]is"+operator[0]+"operator[1]is"+operator[1]);
			if (order == 0) {
				if (operator[0] <= 2 && operator[1] > 2) {
					if (ifbracket) {
						bracket = 1;
					} else {
						operator[1] -= 2;
					}
				}
			} else {
				if (operator[1] < 3) {
					if (ifbracket) {
						bracket = 2;
					} else if (operator[0] < 3) {
						operator[1] += 2;
					} else {
						operator[0] -= 2;
						operator[1] += 2;
					}
				}
				if (operator[0] > 2 && operator[1] > 2) {
					operator[0] -= 2;
				}
			}
			if (order == 0) {
				if (operator[0] == 4 && (x[1].n == 0 && x[1].ifnatural)) {
					ifcorrect = false;
				} else if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
					ifcorrect = false;
				} else {
					answer = cal(x[0], x[1], operator[0]);
					answer = cal(answer, x[2], operator[1]);
				}
			} else {
				if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
					ifcorrect = false;
				} else {
					answer = cal(x[1], x[2], operator[1]);
				}
				if (operator[0] == 4 && (answer.n == 0 && answer.ifnatural)) {
					ifcorrect = false;
				} else {
					answer = cal(x[0], answer, operator[0]);
				}
			}
		} else {
			switch (order) {
				case 0:
					operator[0] = randint(3, 4);
					if (operator[0] == 4 && (x[1].n == 0 && x[1].ifnatural)) {
						ifcorrect = false;
					}
					operator[1] = randint(1, 4);
					if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
						ifcorrect = false;
					}
					operator[2] = randint(1, 2);
					if (ifcorrect) {
						answer = cal(x[0], x[1], operator[0]);
						answer = cal(answer, x[2], operator[1]);
						answer = cal(answer, x[3], operator[2]);
					}
					break;
				case 1:
					operator[0] = randint(3, 4);
					if (operator[0] == 4 && (x[1].n == 0 && x[1].ifnatural)) {
						ifcorrect = false;
					}
					operator[1] = randint(1, 2);
					if (ifbracket) {
						bracket = 1;
						operator[2] = randint(1, 4);
					} else {
						operator[2] = randint(3, 4);
					}
					if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
						ifcorrect = false;
					}
					if (ifcorrect) {
						answer = cal(x[0], x[1], operator[0]);
						answer = cal(answer, cal(x[2], x[3], operator[2]), operator[1]);
					}
					break;
				case 2:
					if (ifbracket) {
						bracket = 2;
						operator[1] = randint(1, 4);
						operator[0] = randint(1, 4);
					} else {
						operator[1] = randint(3, 4);
						operator[0] = randint(1, 2);
					}
					if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
						ifcorrect = false;
					}
					operator[2] = randint(1, 2);
					answer = cal(x[1], x[2], operator[1]);
					answer = cal(x[0], answer, operator[0]);
					answer = cal(answer, x[3], operator[2]);
					break;
				case 3:
					if (ifbracket) {
						operator[1] = randint(1, 4);
						bracket = 2;
					} else {
						operator[1] = randint(3, 4);
					}
					operator[2] = randint(3, 4);
					operator[0] = randint(1, 2);
					if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
						ifcorrect = false;
					}
					if (operator[2] == 4 && (x[3].n == 0 && x[3].ifnatural)) {
						ifcorrect = false;
					}
					answer = cal(x[1], x[2], operator[1]);
					answer = cal(answer, x[3], operator[2]);
					answer = cal(x[0], answer, operator[0]);
					break;
				case 4:
					if (ifbracket) {
						operator[2] = randint(1, 4);
						bracket = 1;
					}
					operator[1] = randint(3, 4);
					operator[0] = randint(1, 2);
					if (operator[1] == 4 && (x[2].n == 0 && x[2].ifnatural)) {
						ifcorrect = false;
					}
					if (operator[2] == 4 && (x[3].n == 0 && x[3].ifnatural)) {
						ifcorrect = false;
					}
					if (ifcorrect) {
						answer = cal(x[2], x[3], operator[2]);
						answer = cal(x[1], answer, operator[1]);
						answer = cal(x[0], answer, operator[0]);
					}
					break;
			}
		}
	}

	void tostringquestion() throws IOException {
		if (numofoperator == 1) {
			numprint1(x[0]);
			writeTxtFile(" " + operator(operator[0]) + " ", "Exercises");
			numprint1(x[1]);
			writeTxtFile("=","Exercises");
		} else if (numofoperator == 2) {
			if (!ifbracket) {
				numprint1(x[0]);
				writeTxtFile(" " + operator(operator[0]) + " ", "Exercises");
				numprint1(x[1]);
				writeTxtFile(" " + operator(operator[1]) + " ", "Exercises");
				numprint1(x[2]);
				writeTxtFile("=", "Exercises");
			} else {
				if (bracket == 1) {
					writeTxtFile("(", "Exercises");
				}
				numprint1(x[0]);
				writeTxtFile(" " + operator(operator[0]) + " ", "Exercises");
				if (bracket == 2) {
					writeTxtFile("(", "Exercises");
				}
				numprint1(x[1]);
				if (bracket == 1) {
					writeTxtFile(")", "Exercises");
				}
				writeTxtFile(" " + operator(operator[1]) + " ", "Exercises");
				numprint1(x[2]);
				if (bracket == 2) {
					writeTxtFile(")", "Exercises");
				}
				writeTxtFile("=", "Exercises");
			}
		} else {
			numprint1(x[0]);
			writeTxtFile(" " + operator(operator[0]) + " ", "Exercises");
			if (bracket == 2) {
				writeTxtFile("(", "Exercises");
			}
			numprint1(x[1]);
			writeTxtFile(" " + operator(operator[1]) + " ", "Exercises");
			if (bracket == 1) {
				writeTxtFile("(", "Exercises");
			}
			numprint1(x[2]);
			if (bracket == 2) {
				writeTxtFile(")", "Exercises");
			}
			writeTxtFile(" " + operator(operator[2]) + " ", "Exercises");
			numprint1(x[3]);
			if (bracket == 1) {
				writeTxtFile(")", "Exercises");
			}
			writeTxtFile("=", "Exercises");
		}
	}

	void tostringanswer() throws IOException
	{
		numprint2(answer);
	}

	public static void writeTxtFile(String newStr,String name) throws IOException {
		try {
			String path=System.getProperty("user.dir");
			File file = new File(path+"\\"+name+".txt");
			FileWriter fw = new FileWriter(file, true);
			fw.write(newStr, 0, newStr.length());
			fw.close();
		} catch (IOException e) {
			System.out.println("序号输出异常");
		}
	}
}