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

        // 상속으로 인해서 Square 를 전달 할 수 있게됨
        client.resize(square, 50, 100);
        /*
            Result
            1. client -> resize
            2. rectangle의 width와 height 를 변경함
            3. 하지만 실제 객체는 정사각혐
            4. 정사각형의 조건을 무너뜨리게 됨
            5. 상속을 잘못 사용하게되는 사례
         */

    }

}