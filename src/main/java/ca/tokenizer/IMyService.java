package ca.tokenizer;

import com.google.inject.ImplementedBy;

/**
 */
@ImplementedBy(MyService.class)
public interface IMyService
{
    /**
     * Retrieves the text to say "Hello World".
     * 
     * @return "Hello World"
     */
    public String getHelloWorldText();
}
