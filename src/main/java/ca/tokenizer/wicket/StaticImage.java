package ca.tokenizer.wicket;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

public class StaticImage extends WebComponent
{
    public StaticImage(final String id, final IModel urlModel)
    {
        super(id, urlModel);
    }

    @Override
    protected void onComponentTag(final ComponentTag tag)
    {
        super.onComponentTag(tag);
        checkComponentTag(tag, "img");
        tag.put("src", getDefaultModelObjectAsString());
    }
}
