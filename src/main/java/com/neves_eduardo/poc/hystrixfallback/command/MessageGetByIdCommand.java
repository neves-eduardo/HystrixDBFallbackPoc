package com.neves_eduardo.poc.hystrixfallback.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.neves_eduardo.poc.hystrixfallback.dto.Message;
import com.neves_eduardo.poc.hystrixfallback.repository.MessageRepository;
import rx.Observable;
import rx.Subscriber;

import java.util.Optional;

public class MessageGetByIdCommand extends HystrixObservableCommand<Optional<Message>> {
    private final MessageRepository messageRepository;
    private final int id;

    public MessageGetByIdCommand(int id, MessageRepository messageRepository) {
        super(HystrixCommandGroupKey.Factory.asKey("message"));
        this.messageRepository=messageRepository;
        this.id=id;
    }


    @Override
    protected Observable<Optional<Message>> construct() {
        return Observable.create(new Observable.OnSubscribe<Optional<Message>>() {
            @Override
            public void call(Subscriber<? super Optional<Message>> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(messageRepository.findById(id));
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        } );
    }

    @Override
    protected Observable<Optional<Message>> resumeWithFallback() {
        return Observable.just(Optional.empty());
    }
}
