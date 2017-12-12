package com.zhaoguhong.blog.common.base;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.EntityMetadata;

public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long>
    implements BaseRepository<T> {

  private final EntityManager entityManager;
  private EntityMetadata entityMetadata;


  // private final Class<T> domainClass;

  // 父类没有不带参数的构造方法，这里手动构造父类
  public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
    // this.domainClass = domainClass;
  }

  public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityMetadata = entityInformation;
    this.entityManager = entityManager;
    // this.provider = PersistenceProvider.fromEntityManager(entityManager);
  }
  // 通过EntityManager来完成查询
  // @Override
  // public List<Object[]> listBySQL(String sql) {
  // return entityManager.createNativeQuery(sql).getResultList();
  // }

  @Override
  public void saveEntity(T entity) {
    entity.setCreateDt(new Date());
    entity.setIsDeleted(0);
    save(entity);
  }

  @Override
  public void updateEntity(T entity) {
    entity.setUpdateDt(new Date());
    save(entity);
  }

  @Override
  public void deteleEntity(T entity) {
    entity.setUpdateDt(new Date());
    entity.setIsDeleted(1);
    save(entity);
  }
}
