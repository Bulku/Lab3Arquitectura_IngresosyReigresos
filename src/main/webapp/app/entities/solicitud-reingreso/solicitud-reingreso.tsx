import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISolicitudReingreso } from 'app/shared/model/solicitud-reingreso.model';
import { getEntities } from './solicitud-reingreso.reducer';

export const SolicitudReingreso = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const solicitudReingresoList = useAppSelector(state => state.solicitudReingreso.entities);
  const loading = useAppSelector(state => state.solicitudReingreso.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="solicitud-reingreso-heading" data-cy="SolicitudReingresoHeading">
        <Translate contentKey="arqui3RealApp.solicitudReingreso.home.title">Solicitud Reingresos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="arqui3RealApp.solicitudReingreso.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/solicitud-reingreso/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="arqui3RealApp.solicitudReingreso.home.createLabel">Create new Solicitud Reingreso</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {solicitudReingresoList && solicitudReingresoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="arqui3RealApp.solicitudReingreso.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.solicitudReingreso.fechaSolicitud">Fecha Solicitud</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.solicitudReingreso.motivo">Motivo</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.solicitudReingreso.estudiante">Estudiante</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {solicitudReingresoList.map((solicitudReingreso, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/solicitud-reingreso/${solicitudReingreso.id}`} color="link" size="sm">
                      {solicitudReingreso.id}
                    </Button>
                  </td>
                  <td>
                    {solicitudReingreso.fechaSolicitud ? (
                      <TextFormat type="date" value={solicitudReingreso.fechaSolicitud} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{solicitudReingreso.motivo}</td>
                  <td>
                    {solicitudReingreso.estudiante ? (
                      <Link to={`/estudiante/${solicitudReingreso.estudiante.id}`}>{solicitudReingreso.estudiante.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/solicitud-reingreso/${solicitudReingreso.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/solicitud-reingreso/${solicitudReingreso.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/solicitud-reingreso/${solicitudReingreso.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="arqui3RealApp.solicitudReingreso.home.notFound">No Solicitud Reingresos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SolicitudReingreso;
