package ca.tokenizer;

import com.google.inject.Singleton;

/**
 * Implementation of IMyService.
 */
@Singleton
public class MyService implements IMyService
{

    /**
     * @see org.apache.wicket.examples.guice.service.IMyService#getHelloWorldText()
     */
    @Override
    public String getHelloWorldText()
    {
        return "Hello World";
    }

}