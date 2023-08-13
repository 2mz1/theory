package com.gngsn.renderer;

import com.gngsn.IRenderer;
import com.gngsn.Message;

public class BodyRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<body>%s</body>".formatted(message);
    }
}