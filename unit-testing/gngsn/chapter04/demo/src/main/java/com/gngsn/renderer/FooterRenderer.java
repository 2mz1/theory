package com.gngsn.renderer;

import com.gngsn.IRenderer;
import com.gngsn.Message;

public class FooterRenderer implements IRenderer {
    @Override
    public String render(final Message message) {
        return "<footer>%s</footer>".formatted(message);
    }
}
