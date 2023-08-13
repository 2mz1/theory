package com.gngsn.renderer;

import com.gngsn.IRenderer;
import com.gngsn.Message;

public class HeaderRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<header>%s</header>".formatted(message);
    }
}
