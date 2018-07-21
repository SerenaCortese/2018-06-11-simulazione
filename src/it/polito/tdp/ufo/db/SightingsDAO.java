package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Arco;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<AnnoAvvistamenti> getYearsSightings() {
		String sql = "SELECT YEAR(datetime) AS year, COUNT(*) AS avvistamenti "
				+ "FROM sighting WHERE country = 'US' "
				+ "GROUP BY YEAR(datetime) ORDER BY YEAR(datetime) ASC" ;
		
		List<AnnoAvvistamenti> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				list.add(new AnnoAvvistamenti(Year.of(res.getInt("year")), res.getInt("avvistamenti"))) ;
			}
			
			conn.close();
			
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getStates(Year anno) {
		String sql = "SELECT DISTINCT state "
				+ "FROM sighting "
				+ "WHERE country = 'us' "
				+ "AND YEAR(datetime) = ? "
				+ "ORDER BY state ASC" ;
		
		List<String> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				list.add(res.getString("state")) ;
			}
			
			conn.close();
			
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public List<Arco> getArchi(Year anno) {
		String sql = "SELECT s1.state as state1, s2.state as state2\n" + 
				"FROM sighting as s1, sighting as s2\n" + 
				"WHERE YEAR(s1.datetime) = YEAR(s2.datetime)\n" + 
				"AND YEAR(s1.datetime) = ?\n" + 
				"AND s1.country = 'us' AND s2.country = 'us'\n" + 
				"AND s1.state <> s2.state\n" + 
				"AND s2.datetime > s1.datetime\n" + 
				"GROUP BY state1,state2\n";
		
		List<Arco> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				list.add(new Arco(res.getString("state1"),res.getString("state2"))) ;
			}
			
			conn.close();
			
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public boolean esisteArco(String s1, String s2, Year anno) {
		String sql = "SELECT COUNT(*) AS count \n" + 
				"FROM sighting as s1, sighting as s2\n" + 
				"WHERE YEAR(s1.datetime) = YEAR(s2.datetime)\n" + 
				"AND YEAR(s1.datetime) = ?\n" + 
				"AND s1.country = 'us' AND s2.country = 'us'\n" + 
				"AND s1.state = ? AND s2.state = ? \n" + 
				"AND s2.datetime > s1.datetime\n";
		
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3,s2);
			ResultSet res = st.executeQuery() ;
			res.next();
			
			int result = res.getInt("count");
			
			conn.close();
			if(result > 0) {
				return true;
			}else
				return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
