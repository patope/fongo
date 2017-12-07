package com.mongodb;

import com.github.fakemongo.Fongo;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import com.mongodb.session.ClientSession;

/**
 * @author VISTALL
 * @since 07-Dec-17
 */
public class FongoOperationExecutor implements OperationExecutor {
  private final Fongo fongo;

  public FongoOperationExecutor(Fongo fongo) {
    this.fongo = fongo;
  }

  @Override
  public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference) {
    return fongo.execute(operation, readPreference);
  }

  @Override
  public <T> T execute(WriteOperation<T> operation) {
    return fongo.execute(operation);
  }

  @Override
  public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference, ClientSession session) {
    return fongo.execute(operation, readPreference);
  }

  @Override
  public <T> T execute(WriteOperation<T> operation, ClientSession session) {
    return fongo.execute(operation);
  }
}
