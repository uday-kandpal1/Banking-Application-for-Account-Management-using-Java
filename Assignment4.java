package html;
import java.io.*;
import java.util.*;
class Book implements Serializable, Comparable<Book> {

    private int bookId;
    private String title;
    private String author;
    private String category;
    private boolean isIssued;

    public Book(Integer bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = false;
    }

    public Integer getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isIssued() { return isIssued; }

    public void markAsIssued() { this.isIssued = true; }
    public void markAsReturned() { this.isIssued = false; }

    public void displayBookDetails() {
        System.out.println("\n--- Book Details ---");
        System.out.println("ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Category: " + category);
        System.out.println("Issued: " + (isIssued ? "Yes" : "No"));
    }
    public int compareTo(Book b) {
        return this.title.compareToIgnoreCase(b.title);
    }
}
class Member implements Serializable {

    private Integer memberId;
    private String name;
    private String email;
    private List<Integer> issuedBooks;

    public Member(Integer memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.issuedBooks = new ArrayList<>();
    }

    public Integer getMemberId() { return memberId; }
    public List<Integer> getIssuedBooks() { return issuedBooks; }

    public void addIssuedBook(int bookId) {
        issuedBooks.add(bookId);
    }

    public void returnIssuedBook(int bookId) {
        issuedBooks.remove(Integer.valueOf(bookId));
    }

    public void displayMemberDetails() {
        System.out.println("\n--- Member Details ---");
        System.out.println("ID: " + memberId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Issued Books: " + issuedBooks);
    }
}
class LibraryManager {

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();

    private final String BOOK_FILE = "books.dat";
    private final String MEMBER_FILE = "members.dat";

    public LibraryManager() {
        loadData();
    }

    // ------------ Load & Save (File Handling) ------------
    @SuppressWarnings("unchecked")
    public void loadData() {
        try (ObjectInputStream oisBook = new ObjectInputStream(new FileInputStream(BOOK_FILE));
             ObjectInputStream oisMember = new ObjectInputStream(new FileInputStream(MEMBER_FILE))) {

            books = (Map<Integer, Book>) oisBook.readObject();
            members = (Map<Integer, Member>) oisMember.readObject();

        } catch (Exception ignored) {}
    }

    public void saveData() {
        try (ObjectOutputStream oosBook = new ObjectOutputStream(new FileOutputStream(BOOK_FILE));
             ObjectOutputStream oosMember = new ObjectOutputStream(new FileOutputStream(MEMBER_FILE))) {

            oosBook.writeObject(books);
            oosMember.writeObject(members);

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
    // ---------------- Core Operations ----------------

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        saveData();
    }

    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        saveData();
    }

    public void issueBook(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);

        if (b == null || m == null) {
            System.out.println("Invalid Book ID or Member ID.");
            return;
        }

        if (b.isIssued()) {
            System.out.println("Book already issued.");
            return;
        }

        b.markAsIssued();
        m.addIssuedBook(bookId);
        saveData();

        System.out.println("Book issued successfully.");
    }

    public void returnBook(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);

        if (b == null || m == null) {
            System.out.println("Invalid Book ID or Member ID.");
            return;
        }

        if (!b.isIssued()) {
            System.out.println("Book is not issued.");
            return;
        }

        b.markAsReturned();
        m.returnIssuedBook(bookId);
        saveData();

        System.out.println("Book returned successfully.");
    }

    public void searchBooks(String keyword) {
        books.values().stream()
             .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
             .forEach(Book::displayBookDetails);
    }

    public void sortBooks() {
        List<Book> list = new ArrayList<>(books.values());
        Collections.sort(list); // uses compareTo(title)
        list.forEach(Book::displayBookDetails);
    }
}

public class Main {

    public static void main(String[] args) {

        LibraryManager manager = new LibraryManager();
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("Welcome to City Library Digital Management System");

        do {
            System.out.println("\n1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Sort Books");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    sc.nextLine();
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();

                    int id = 100 + (int) (Math.random() * 900);
                    manager.addBook(new Book(id, title, author, category));
                    System.out.println("Book added with ID: " + id);
                    break;

                case 2:
                    sc.nextLine();
                    System.out.print("Enter Member Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    int mId = 500 + (int) (Math.random() * 900);
                    manager.addMember(new Member(mId, name, email));
                    System.out.println("Member added with ID: " + mId);
                    break;

                case 3:
                    System.out.print("Enter Book ID: ");
                    int bIdIssue = sc.nextInt();
                    System.out.print("Enter Member ID: ");
                    int mIdIssue = sc.nextInt();
                    manager.issueBook(bIdIssue, mIdIssue);
                    break;

                case 4:
                    System.out.print("Enter Book ID: ");
                    int bIdReturn = sc.nextInt();
                    System.out.print("Enter Member ID: ");
                    int mIdReturn = sc.nextInt();
                    manager.returnBook(bIdReturn, mIdReturn);
                    break;

                case 5:
                    sc.nextLine();
                    System.out.print("Enter keyword: ");
                    String key = sc.nextLine();
                    manager.searchBooks(key);
                    break;

                case 6:
                    manager.sortBooks();
                    break;

                case 7:
                    System.out.println("Exiting...");
                    manager.saveData();
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 7);

        sc.close();
    }
}


