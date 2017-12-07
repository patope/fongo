package com.github.fakemongo.async;

import com.github.fakemongo.FongoConnection;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.connection.*;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 */
class FongoAsyncConnection implements AsyncConnection {
  private final static Logger LOG = LoggerFactory.getLogger(FongoAsyncConnection.class);

  private final FongoAsync fongoAsync;
  private final FongoConnection fongoConnection;
  private final ConnectionDescription connectionDescription;

  public FongoAsyncConnection(final FongoAsync fongoAsync) {
    this.fongoAsync = fongoAsync;
    this.connectionDescription = new ConnectionDescription(new ServerId(new ClusterId(), fongoAsync.getServerAddress())) {
      @Override
      public ServerVersion getServerVersion() {
        return fongoAsync.getServerVersion();
      }
    };
    this.fongoConnection = new FongoConnection(fongoAsync.getFongo());
  }

  @Override
  public FongoAsyncConnection retain() {
    LOG.debug("retain()");
    return this;
  }

  @Override
  public ConnectionDescription getDescription() {
    return connectionDescription;
  }

  @Override
  public void insertAsync(MongoNamespace namespace, boolean ordered, InsertRequest insertRequest, SingleResultCallback<WriteConcernResult> callback) {
    insertAsync(namespace, ordered, WriteConcern.UNACKNOWLEDGED, Arrays.asList(insertRequest), callback);
  }

  @Override
  public void updateAsync(MongoNamespace namespace, boolean ordered, UpdateRequest updateRequest, SingleResultCallback<WriteConcernResult> callback) {
    updateAsync(namespace, ordered, WriteConcern.UNACKNOWLEDGED, Arrays.asList(updateRequest), callback);
  }

  @Override
  public void deleteAsync(MongoNamespace namespace, boolean ordered, DeleteRequest deleteRequest, SingleResultCallback<WriteConcernResult> callback) {
    deleteAsync(namespace, ordered, WriteConcern.UNACKNOWLEDGED, Arrays.asList(deleteRequest), callback);
  }

  @Override
  public <T> void commandAsync(final String database, final BsonDocument command, final FieldNameValidator fieldNameValidator, final ReadPreference readPreference, final Decoder<T> commandResultDecoder, final SessionContext sessionContext, final SingleResultCallback<T> callback) {
    LOG.info("commandAsync() command:{}", command);
    asyncResult(new Callable<T>() {
      @Override
      public T call() throws Exception {
        return fongoConnection.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, sessionContext);
      }
    }, callback);  }

  @Override
  public <T> void commandAsync(final String database, final BsonDocument command, final FieldNameValidator commandFieldNameValidator, final ReadPreference readPreference, final Decoder<T> commandResultDecoder, final SessionContext sessionContext, final boolean responseExpected, final SplittablePayload payload, final FieldNameValidator payloadFieldNameValidator, SingleResultCallback<T> callback) {
    LOG.info("commandAsync() command:{}", command);
    asyncResult(new Callable<T>() {
      @Override
      public T call() throws Exception {
        return fongoConnection.command(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, sessionContext, responseExpected, payload, payloadFieldNameValidator);
      }
    }, callback);
  }

  public void insertAsync(final MongoNamespace namespace, final boolean ordered, final WriteConcern writeConcern, final List<InsertRequest> inserts, SingleResultCallback<WriteConcernResult> callback) {
    asyncResult(new Callable<WriteConcernResult>() {
      @Override
      public WriteConcernResult call() throws Exception {
        return fongoConnection.insert(namespace, ordered, writeConcern, inserts);
      }
    }, callback);
  }

  public void updateAsync(final MongoNamespace namespace, final boolean ordered, final WriteConcern writeConcern, final List<UpdateRequest> updates, SingleResultCallback<WriteConcernResult> callback) {
    asyncResult(new Callable<WriteConcernResult>() {
      @Override
      public WriteConcernResult call() throws Exception {
        return fongoConnection.update(namespace, ordered, writeConcern, updates);
      }
    }, callback);
  }

  public void deleteAsync(final MongoNamespace namespace, final boolean ordered, final WriteConcern writeConcern, final List<DeleteRequest> deletes, SingleResultCallback<WriteConcernResult> callback) {
    asyncResult(new Callable<WriteConcernResult>() {
      @Override
      public WriteConcernResult call() throws Exception {
        return fongoConnection.delete(namespace, ordered, writeConcern, deletes);
      }
    }, callback);
  }

  @Override
  public <T> void commandAsync(final String database, final BsonDocument command, final boolean slaveOk, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, SingleResultCallback<T> callback) {
    LOG.info("commandAsync() command:{}", command);
    asyncResult(new Callable<T>() {
      @Override
      public T call() throws Exception {
        return fongoConnection.command(database, command, slaveOk, fieldNameValidator, commandResultDecoder);
      }
    }, callback);
  }

  @Override
  public <T> void queryAsync(final MongoNamespace namespace, final BsonDocument queryDocument, final BsonDocument fields, final int numberToReturn, final int skip, final boolean slaveOk, final boolean tailableCursor, final boolean awaitData, final boolean noCursorTimeout, final boolean partial, final boolean oplogReplay, final Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
    asyncResult(new Callable<QueryResult<T>>() {
      @Override
      public QueryResult<T> call() throws Exception {
        return fongoConnection.query(namespace, queryDocument, fields, numberToReturn, skip, slaveOk, tailableCursor, awaitData, noCursorTimeout, partial, oplogReplay, resultDecoder);
      }
    }, callback);
  }

  @Override
  public <T> void queryAsync(final MongoNamespace namespace, final BsonDocument queryDocument, final BsonDocument fields, final int skip, final int limit, final int batchSize, final boolean slaveOk, final boolean tailableCursor, final boolean awaitData, final boolean noCursorTimeout, final boolean partial, final boolean oplogReplay, final Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
    LOG.info("queryAsync {}", queryDocument);
    asyncResult(new Callable<QueryResult<T>>() {
      @Override
      public QueryResult<T> call() throws Exception {
        return fongoConnection.query(namespace, queryDocument, fields, skip, limit, batchSize, slaveOk, tailableCursor, awaitData, noCursorTimeout, partial, oplogReplay, resultDecoder);
      }
    }, callback);
  }

  @Override
  public <T> void getMoreAsync(final MongoNamespace namespace, final long cursorId, final int numberToReturn, final Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
    asyncResult(new Callable<QueryResult<T>>() {
      @Override
      public QueryResult<T> call() throws Exception {
        return fongoConnection.getMore(namespace, cursorId, numberToReturn, resultDecoder);
      }
    }, callback);
  }

  @Override
  public void killCursorAsync(final List<Long> cursors, SingleResultCallback<Void> callback) {
    asyncResult(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        fongoConnection.killCursor(cursors);
        return null;
      }
    }, callback);
  }

  @Override
  public void killCursorAsync(final MongoNamespace namespace, final List<Long> cursors, SingleResultCallback<Void> callback) {
    asyncResult(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        fongoConnection.killCursor(namespace, cursors);
        return null;
      }
    }, callback);
  }

  private <T> void asyncResult(Callable<T> callable, SingleResultCallback<T> callback) {
    try {
      callback.onResult(callable.call(), null);
    }
    catch (Throwable throwable) {
      callback.onResult(null, throwable);
    }
  }

  @Override
  public int getCount() {
    LOG.info("getCount()");
    return 0;
  }

  @Override
  public void release() {
    LOG.debug("release()");
  }

}
