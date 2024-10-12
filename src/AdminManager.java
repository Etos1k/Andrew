import java.io.*;
import java.util.List;

public class AdminManager extends  UserManager{
    private static final String FILE_NAME = "users.dat";
    private List<User> users;

public AdminManager() {
    users = loadUsers();
}
public String deleteUser(String email, String password) {
    if (authenticateUser(email, password)) {
        try {
            // Прочитать всех пользователей
            List<User> users = readUsersFromFile();

            // Удалить пользователя с указанным email
            users.removeIf(user -> user.getEmail().equals(email));

            // Перезаписать файл без удаленного пользователя
            writeUsersToFile(users);

            return "Пользователь успешно удален";
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
    public void registerAdmin(String email, String password, boolean isAdmin) {
        if (getUserByEmail(email) != null) {
            System.out.println("Пользователь с таким именем уже существует.");
            return;
        }

        User newUser = new User(email, password, isAdmin);
        users.add(newUser);
        saveUsers();
        System.out.println("Пользователь успешно зарегистрирован.");
    }
}
