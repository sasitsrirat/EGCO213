class Process {
	public String name; // +
	public int price; // +
	public int totalDishes; // +

	public static void main(String args[]) throws IOException {
		Scanner textInput2 = new Scanner(System.in);
		Scanner input2 = new Scanner(new File("C:\\Users\\SasitSrirat\\Desktop\\Homework\\Homework\\text\\Orders.txt"));
		Scanner input1 = new Scanner(new File("C:\\Users\\SasitSrirat\\Desktop\\Homework\\Homework\\text\\Menus.txt"));
		System.out.println(" === Order Processing ===");
		while (input2.hasNext())

		{
			String order = input2.nextLine();
			String menus = input1.next();
			String splited[] = order.split(",");
			String splited2[] = menus.split(",");

			try {
				if (splited.length != 5) {
					throw (new ArrayIndexOutOfBoundsException(splited.length));
				}
				/*
				 * if (splited2.length != 5) { throw (new
				 * ArrayIndexOutOfBoundsException(splited2.length)); }
				 */
			}

			catch (ArrayIndexOutOfBoundsException e) {

				// System.out.print(" " + splited[0] );
				System.out.println("  " + splited[0] + " >> Order BBQ Pork" + "(" + splited[1] + ")"
						+ " Hainanese Chicken Rice" + "(" + splited[1] + ")" + " Roasted Duck with Rice" + "("
						+ splited[1] + ")" + " Vegetarian Fried Rice" + "(" + splited[1] + ")" + " Wonton Soup" + "("
						+ splited[1] + ")");
				System.out.print("Order Bill = ");
				System.out.print("\tCurrent Point = ");
				System.out.print("\tFree  = ");

				// System.out.print(" " + splited2[0] + "(" + splited[1]+" " + ")");
				// System.out.println(" >> " + menus );
			}
			// System.out.print(" " + splited2[0] + "(" + splited[1]+" " + ")");

		}

	}
}