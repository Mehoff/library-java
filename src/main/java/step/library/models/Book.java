package step.library.models;

public class Book {
    private String id;
    private String author;
    private String title;
    private String description;
    private String cover;

    public Book(String author, String title, String description, String cover){
        this.author = author;
        this.title = title;
        this.description = description;
        this.cover = cover;
    }

    public Book(String id, String author, String title, String description, String cover){
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.cover = cover;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }
}
