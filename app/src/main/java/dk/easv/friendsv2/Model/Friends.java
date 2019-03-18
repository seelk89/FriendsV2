package dk.easv.friendsv2.Model;

import java.util.ArrayList;

public class Friends {

	ArrayList<BEFriend> m_friends;
	
	public Friends()
	{
		m_friends = new ArrayList<BEFriend>();
		m_friends.add(new BEFriend("Alex", "123456", true));
		m_friends.add(new BEFriend("Anni", "234567"));
		m_friends.add(new BEFriend("Armandas", "126256", true));
		m_friends.add(new BEFriend("Asger", "234567"));
		m_friends.add(new BEFriend("Attila", "123456"));
		m_friends.add(new BEFriend("Bastian", "994567"));
		m_friends.add(new BEFriend("Bence", "127426"));
		m_friends.add(new BEFriend("Bo", "204587", true));
		m_friends.add(new BEFriend("Daniel", "123456"));
		m_friends.add(new BEFriend("David", "234567", true));
		m_friends.add(new BEFriend("Dominik", "123456"));
		m_friends.add(new BEFriend("Jonas", "234567"));
		m_friends.add(new BEFriend("Emil", "123456"));
		m_friends.add(new BEFriend("Fabio", "234567", true));
		m_friends.add(new BEFriend("Frederik", "123456"));
		m_friends.add(new BEFriend("Jacob", "234567"));
		m_friends.add(new BEFriend("Jan", "123456"));
		m_friends.add(new BEFriend("Jesper", "234567"));
		m_friends.add(new BEFriend("Kasper", "123456"));
		m_friends.add(new BEFriend("kenneth", "234567", true));
		m_friends.add(new BEFriend("Mads", "123456"));
		m_friends.add(new BEFriend("Marius", "234567"));
		m_friends.add(new BEFriend("Edwin", "234567"));
		m_friends.add(new BEFriend("Michal", "123456"));
		m_friends.add(new BEFriend("Morten", "234567", true));
		m_friends.add(new BEFriend("Nicolai", "123456"));
		m_friends.add(new BEFriend("Oliver", "234567"));
	}
	
	public ArrayList<BEFriend> getAll()
	{ return m_friends; }
	
	public String[] getNames()
	{
		String[] res = new String[m_friends.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = m_friends.get(i).getName();
		}
		return res;
	}

}
