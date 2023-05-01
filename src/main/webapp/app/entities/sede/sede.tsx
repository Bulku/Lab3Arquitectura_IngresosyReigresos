import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISede } from 'app/shared/model/sede.model';
import { getEntities } from './sede.reducer';

export const Sede = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const sedeList = useAppSelector(state => state.sede.entities);
  const loading = useAppSelector(state => state.sede.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="sede-heading" data-cy="SedeHeading">
        <Translate contentKey="arqui3RealApp.sede.home.title">Sedes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="arqui3RealApp.sede.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/sede/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="arqui3RealApp.sede.home.createLabel">Create new Sede</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sedeList && sedeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="arqui3RealApp.sede.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.sede.nombre">Nombre</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.sede.direccion">Direccion</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sedeList.map((sede, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sede/${sede.id}`} color="link" size="sm">
                      {sede.id}
                    </Button>
                  </td>
                  <td>{sede.nombre}</td>
                  <td>{sede.direccion}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/sede/${sede.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/sede/${sede.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/sede/${sede.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="arqui3RealApp.sede.home.notFound">No Sedes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Sede;
