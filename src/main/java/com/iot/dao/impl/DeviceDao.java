package com.iot.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.iot.dao.IDeviceDao;
import com.iot.entity.DeviceEntity;

@Repository
public class DeviceDao extends AbstractDao<Long, DeviceEntity> implements IDeviceDao {

	@Override
	public DeviceEntity findByIdAndUsername(String username, Long id) {
		DeviceEntity result = null;
		try {
			String sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t.userEntity u"
					+ " where u.username=:username and t.id=:id";

			Query q = entityManager.createQuery(sql);
			q.setParameter("username", username);
			q.setParameter("id", id);
			result = (DeviceEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public DeviceEntity findByIdAndUserID(Long userID, Long id) {
		DeviceEntity result = null;
		try {
			String sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t.userEntity u"
					+ " where u.id=:userid and t.id=:id";

			Query q = entityManager.createQuery(sql);
			q.setParameter("userid", userID);
			q.setParameter("id", id);
			result = (DeviceEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public DeviceEntity findByIdWithProp(Long id, String JOIN_FETCH, int status, String username) {
		DeviceEntity result = null;
		if (StringUtils.isNotBlank(username)) {
			String sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t." + JOIN_FETCH
					+ " JOIN FETCH t.userEntity u where t.id=:id and u.username=:username";
			if (status == 1) { 
				sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t." + JOIN_FETCH
						+ " JOIN FETCH t.userEntity u where t.id=:id and s.status=1 and u.username=:username";
			}
			try {
				Query q = entityManager.createQuery(sql);
				q.setParameter("id", id);
				q.setParameter("username", username);
				result = (DeviceEntity) q.getSingleResult();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			String sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t." + JOIN_FETCH
					+ " where t.id=:id";
			if (status == 1) {
				sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t." + JOIN_FETCH
						+ " s where t.id=:id and s.status=1";
			}
			try {
				Query q = entityManager.createQuery(sql);
				q.setParameter("id", id);
				result = (DeviceEntity) q.getSingleResult();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

	@Transactional
	@Override
	public void updateKeepAlive(List<Long> ids) {
		try {
			Query q = null;
			if (ids.size() != 0) {
				String sql1 = "UPDATE " + getPersistenceClassName()
						+ " e SET e.alive=1,e.updated_at=:datenow where e.id IN(";
				int flag = 0;
				for (Long id : ids) {
					if (flag == 0) {
						sql1 += id.toString();
						flag++;
					} else {
						sql1 += "," + id.toString();
					}
				}
				sql1 += ")";

				/*
				 * chỉ cập nhật những thằng nào alive=1 khi mà ko thuộc IN
				 */
				q = entityManager.createQuery(sql1);
				q.setParameter("datenow", new Date());
				q.executeUpdate();
				String sql2 = "UPDATE " + getPersistenceClassName()
						+ " e SET e.alive=0,e.updated_at=:datenow where e.id NOT IN(";
				flag = 0;
				for (Long id : ids) {
					if (flag == 0) {
						sql2 += id.toString();
						flag++;
					} else {
						sql2 += "," + id.toString();
					}
				}
				sql2 += ") and e.alive=1";
				q = entityManager.createQuery(sql2);
				q.setParameter("datenow", new Date());
				q.executeUpdate();
			} else {
				String sql1 = "UPDATE " + getPersistenceClassName()
						+ " e SET e.alive=0, e.updated_at=:datenow where e.alive !=0";
				q = entityManager.createQuery(sql1);
				q.setParameter("datenow", new Date());
				q.executeUpdate();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceEntity> getListDeviceByUser(String username) {
		List<DeviceEntity> result = new ArrayList<DeviceEntity>();
		try {
			String sql = "SELECT t from " + getPersistenceClassName()
					+ " t JOIN FETCH t.userEntity u where u.username=:username";
			Query q = entityManager.createQuery(sql);
			q.setParameter("username", username);
			result = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceEntity> getListDeviceByAdmin() {
		List<DeviceEntity> result = new ArrayList<DeviceEntity>();
		try {
			String sql = "SELECT t from " + getPersistenceClassName() + " t JOIN FETCH t.userEntity";
			Query q = entityManager.createQuery(sql);
			result = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public DeviceEntity findByIdUser(Long id) {
		DeviceEntity result = null;
		try {
			String sql = "Select t from " + getPersistenceClassName() + " t" + " JOIN FETCH t.userEntity u"
					+ " where t.id=:id";

			Query q = entityManager.createQuery(sql);
			q.setParameter("id", id);
			result = (DeviceEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
