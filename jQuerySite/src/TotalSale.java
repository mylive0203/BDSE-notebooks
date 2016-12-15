

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.sun.swing.internal.plaf.metal.resources.metal;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedList; 
import java.util.Map;
/**
 * Servlet implementation class Products
 */
@WebServlet("/TotalSale")
public class TotalSale extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TotalSale() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Northwind";
		
		String query = "SELECT YEAR(Orders.OrderDate) as SalesYear,Month(Orders.OrderDate) as SalesMonth,Sum([Order Details].UnitPrice * [Order Details].Quantity * [Order Details].Discount) as TotalSales FROM Orders INNER JOIN [Order Details] ON Orders.OrderID = [Order Details].OrderID Where YEAR(Orders.OrderDate) = ? GROUP BY YEAR(Orders.OrderDate), Month(Orders.OrderDate) ORDER BY YEAR(Orders.OrderDate), Month(Orders.OrderDate)";
		
		String syear = request.getParameter("syear");
		if(syear == null){
			syear="1997";
		}

		try{
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			// conn = DriverManager.getConnection(url, "sa", "sa123456");
						conn = DriverManager.getConnection(url, "sa", "P@ssw0rd");
					
			stmt = conn.prepareStatement(query);
			 stmt.setString(1,syear);
			 
			 rs = stmt.executeQuery();
			
			 List  l1 = new LinkedList();
			 while (rs.next()) {
				 Map m1 = new HashMap();       
				 m1.put("SalesYear",rs.getString(1));   
				 m1.put("SalesMonth", rs.getString(2)); 
				 m1.put("TotalSales",rs.getString(3)); 
				 l1.add(m1);
			 }
			
			 String jsonString = JSONValue.toJSONString(l1);   
			
			 out.println(jsonString);
		}catch (SQLException e) {
			out.println("Error:" + e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
