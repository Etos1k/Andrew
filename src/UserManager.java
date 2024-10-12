import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String FILE_NAME = "users.dat";
    private List<User> users;

    public UserManager() {
        users = loadUsers();
    }

    public void registerUser(String email, String password) {
        if (getUserByEmail(email) != null) {
            System.out.println("Пользователь с таким именем уже существует.");
            return;
        }

        User newUser = new User(email, password);
        users.add(newUser);
        saveUsers();
        System.out.println("Пользователь успешно зарегистрирован.");
    }

    public boolean authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Авторизация успешна.");
            return true;
        }
        System.out.println("Неверное имя пользователя или пароль.");
        return false;
    }


    User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Если файл не существует или произошла ошибка, просто возвращаем пустой список
        }
        return users;
    }
    public String deleteUser(String email, String password) {
        if (authenticateUser(email, password)) {
            try {
                // Прочитать всех пользователей
                List<User> users = readUsersFromFile();

                // Удалить пользователя с указанным email
                boolean removed = users.removeIf(user -> user.getEmail().equals(email));

                if (removed) {
                    // Перезаписать файл без удаленного пользователя
                    writeUsersToFile(users);
                    return "Пользователь успешно удален";
                } else {
                    return "Пользователь не найден";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка при удалении пользователя";
            }
        } else {
            return "Ошибка авторизации";
        }
    }
    private List<User> readUsersFromFile() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<User>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Ошибка при чтении пользователей", e);
        }
    }
    private void writeUsersToFile(List<User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        }
    }
    public String changeEmail(String oldEmail, String password, String newEmail) {
        if (authenticateUser(oldEmail, password)) {
            try {
                // Прочитать всех пользователей
                List<User> users = readUsersFromFile();

                // Найти пользователя и изменить его email
                for (User user : users) {
                    if (user.getEmail().equals(oldEmail)) {
                        user.setEmail(newEmail);
                        break;
                    }
                }

                // Перезаписать файл с обновленными данными
                writeUsersToFile(users);

                return "Email успешно изменен";
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка при изменении email";
            }
        } else {
            return "Ошибка авторизации";
        }
    }

    public String changePassword(String email, String oldPassword, String newPassword) {
        if (authenticateUser(email, oldPassword)) {
            try {
                // Прочитать всех пользователей
                List<User> users = readUsersFromFile();

                // Найти пользователя и изменить его пароль
                for (User user : users) {
                    if (user.getEmail().equals(email)) {
                        user.setPassword(newPassword);
                        break;
                    }
                }

                // Перезаписать файл с обновленными данными
                writeUsersToFile(users);

                return "Пароль успешно изменен";
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка при изменении пароля";
            }
        } else {
            return "Ошибка авторизации";
        }
    }
}
