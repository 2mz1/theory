package liskov.v1;

public class Client {

    public void resize(Rectangle rectangle, int width, int height) {
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        assert rectangle.getWidth() == width && rectangle.getHeight() == height;
    }

    public static void main(String[] args) {
        // Client resize 의 Rectangle 에 Square 를 전달 하면?
        Square square = new Square(10, 10, 10);
        Client client = new Client();

        client.resize(square, 50, 100);
    }

}