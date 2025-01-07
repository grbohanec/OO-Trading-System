package user;

import java.util.HashMap;
import java.util.Random;

import exceptions.BadValueException;
import exceptions.NullParamException;
import tradable.TradableDTO;
import exceptions.DataValidationException;


public class UserManager {
    private static UserManager instance;
    private HashMap<String, User> users;

    private UserManager() {
        users = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void init(String[] usersIn) throws DataValidationException, NullParamException, BadValueException {
        if (usersIn == null) {
            throw new DataValidationException("usersIn is null!");
        }
        for (String userId : usersIn) {
            users.put(userId, new User(userId));
        }
    }

    public User getRandomUser() {
        if (users.isEmpty()) {
            return null;
        }
        Object[] userIds = users.keySet().toArray();
        String randomId = (String) userIds[new Random().nextInt(userIds.length)];
        return users.get(randomId);
    }

    public void addToUser(String userId, TradableDTO o) throws DataValidationException {
        if (userId == null) {
            throw new DataValidationException("UserId is null!");
        }
        if (o == null) {
            throw new DataValidationException("TradableDTO is null!");
        }

        User user = users.get(userId);
        if (user == null) {
            throw new DataValidationException("User does not exist!");
        }

        user.addTradable(o);
    }

    public User getUser(String id) {
        User user =  users.get(id);
        if (users.containsValue(user)) {
            return user;
        }

        return null;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : users.values()) {
            sb.append(user).append("\n");
        }
        return sb.toString();
    }

}
