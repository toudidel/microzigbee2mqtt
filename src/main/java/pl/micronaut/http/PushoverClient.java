package pl.micronaut.http;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import pl.micronaut.model.PushoverMessage;

import static io.micronaut.http.HttpHeaders.CONTENT_TYPE;

@Client("https://api.pushover.net/1")
@Header(name = CONTENT_TYPE, value = "application/json")
public interface PushoverClient {

  @Post("/messages.json")
  void send(@Body PushoverMessage message);
}
