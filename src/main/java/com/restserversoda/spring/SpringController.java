package com.restserversoda.spring;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringController {

	Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy HH:mm:ss").create();
	@GetMapping("/")
	public String test()
	{
		try(InputStream inputStream = new FileInputStream("src/main/data/data.json");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
		{
			return bufferedReader.readLine();
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
	}

	@GetMapping("/fetchuser/{username}")
	public String fetchUser(@PathVariable String username) {

		synchronized (this) {
			try {
				List<UserModel> users = gson.fromJson(new FileReader("src/main/data/data.json"), new TypeToken<List<UserModel>>(){}.getType());
				if(users == null || users.isEmpty())
				{
					return "";
				}
				for(UserModel user : users)
				{
					if(user.getUsername().equals(username))
					{
						String result = gson.toJson(user);
						return result;
					}
				}
				return "";
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return "";
			}
		}
	}
	@PostMapping("/syncdb/")
	public int syncDb(@RequestBody String user)
	{
		synchronized (this)
		{
			try (Reader reader = new FileReader("src/main/data/data.json")){
				List<UserModel> users = gson.fromJson(reader, new TypeToken<List<UserModel>>(){}.getType());
				UserModel _user = gson.fromJson(user, UserModel.class);
				if(users == null || users.isEmpty())
				{
					users = new ArrayList<>();
					users.add(_user);
					try(Writer writer = new FileWriter("src/main/data/data.json",false))
					{
						gson.toJson(users, writer);
					}
					return 0;
				}
				int i;
				boolean found = false;
				for(i=0; i<users.size(); i++)
				{
					if(users.get(i).getUsername().equals(_user.getUsername()))
					{
						found = true;
						break;
					}
				}
				if(!found)
				{
					users.add(_user);
				}
				else
				{
					users.remove(i);
					users.add(_user);
				}
				try(Writer writer = new FileWriter("src/main/data/data.json",false))
				{
					gson.toJson(users, writer);
				}
				return 0;
			}
			catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	@GetMapping("/getstats/{username}")
	public String getStats(@PathVariable String username)
	{
		synchronized (this)
		{
			try (Reader reader = new FileReader("src/main/data/data.json")) {
				List<UserModel> users = gson.fromJson(reader, new TypeToken<List<UserModel>>(){}.getType());
				if(users == null || users.isEmpty())
				{
					return "";
				}
				UserModel user = null;
				for(UserModel u : users)
				{
					if(username.equals(u.getUsername()))
					{
						user = u;
						break;
					}
				}
				if(user == null)
				{
					return "";
				}
                // age
                List<UserModel> ageList = new ArrayList();
				List<UserModel> genderList = new ArrayList();
				List<UserModel> conditionList = new ArrayList();
				List<UserModel> occupationList = new ArrayList<>();

				for(UserModel u : users)
                {
                    if(Math.abs(u.getAge()-user.getAge()) <= 2)
					{
						ageList.add(u);
					}
                    if(u.getGender().equals(user.getGender()))
					{
						genderList.add(u);
					}
                    if(u.getCondition().equals(user.getCondition()))
					{
						conditionList.add(u);
					}
                    if(u.getOccupation().equals(user.getOccupation()))
					{
						occupationList.add(u);
					}
                }
				ageList.sort(new CustomComparator());
				genderList.sort(new CustomComparator());
				conditionList.sort(new CustomComparator());
				occupationList.sort(new CustomComparator());
				StatsModel stats = new StatsModel();
				stats.setCumScore(user.getScore());
				stats.setOccupationPercent(100.0);
				stats.setConditionPercent(100.0);
				stats.setGenderPercent(100.0);
				stats.setAgePercent(100.0);
				for(int i=0; i<occupationList.size(); i++)
				{
					if(occupationList.get(i).getUsername().equals(username))
					{
						stats.setOccupationPercent((i+1) * 100 / occupationList.size());
						break;
					}
				}
				for(int i=0; i<ageList.size(); i++)
				{
					if(ageList.get(i).getUsername().equals(username))
					{
						stats.setAgePercent((i+1) * 100 / ageList.size());
						break;
					}
				}
                // gender
				for(int i=0; i<genderList.size(); i++)
				{
					if(genderList.get(i).getUsername().equals(username))
					{
						stats.setGenderPercent((i+1) * 100 / genderList.size());
						break;
					}
				}
				for(int i=0; i<conditionList.size(); i++)
				{
					if(conditionList.get(i).getUsername().equals(username))
					{
						stats.setConditionPercent((i+1) * 100 / conditionList.size());
						break;
					}
				}
				String result = gson.toJson(stats);
				return result;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return "";
			}
		}
	}
	class CustomComparator implements Comparator<UserModel>
    {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return (int)(o1.getScore() - o2.getScore());
        }
    }
}
