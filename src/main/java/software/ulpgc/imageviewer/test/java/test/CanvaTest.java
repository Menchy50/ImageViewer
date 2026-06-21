package test;

import software.ulpgc.imageviewer.architecture.Canvas;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CanvasTest {

    @Test
    public void given_image_smaller_than_canvas_should_return_image_size() {
        Canvas canvas = Canvas.ofSize(200,200)
                .fit(100,100);
        assertThat(canvas).isEqualTo(Canvas.ofSize(100,100));
    }

    @Test
    public void given_image_wider_than_canvas_should_return_image_scaled() {
        Canvas canvas = Canvas.ofSize(200,200)
                .fit(400,100);
        assertThat(canvas).isEqualTo(Canvas.ofSize(200,50));
    }

    @Test
    public void given_image_higher_than_canvas_should_return_image_scaled() {
        Canvas canvas = Canvas.ofSize(200,200)
                .fit(100,400);
        assertThat(canvas).isEqualTo(Canvas.ofSize(50,200));
    }

    @Test
    public void given_image_bigger_than_canvas_should_return_image_scaled() {
        Canvas canvas = Canvas.ofSize(200,200)
                .fit(400,800);
        assertThat(canvas).isEqualTo(Canvas.ofSize(100,200));
    }


}
