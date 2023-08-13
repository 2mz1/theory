package com.gngsn;

import com.gngsn.renderer.BodyRenderer;
import com.gngsn.renderer.FooterRenderer;
import com.gngsn.renderer.HeaderRenderer;
import org.junit.jupiter.api.Test;

import java.util.List;

class MessageRendererTest {

    @Test
    void MessageRenderer_uses_correct_sub_renderers() {
        var sut = new MessageRenderer();

        List<IRenderer> renderers = sut.SubRenderers;

        assert 3 == renderers.size();
        assert renderers.get(0) instanceof HeaderRenderer;
        assert renderers.get(1) instanceof BodyRenderer;
        assert renderers.get(3) instanceof FooterRenderer;
    }
}