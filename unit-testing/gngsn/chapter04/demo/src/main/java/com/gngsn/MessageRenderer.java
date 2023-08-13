package com.gngsn;

import com.gngsn.renderer.BodyRenderer;
import com.gngsn.renderer.FooterRenderer;
import com.gngsn.renderer.HeaderRenderer;

import java.util.List;
import java.util.stream.Collectors;

public class MessageRenderer implements IRenderer {

    public List<IRenderer> SubRenderers;

    public MessageRenderer() {
        SubRenderers = List.of(
                new HeaderRenderer(),
                new BodyRenderer(),
                new FooterRenderer()
        );
    }

    @Override
    public String render(Message message) {
        return SubRenderers
                .stream()
                .map(x -> x.render(message))
                .collect(Collectors.joining());
    }
}