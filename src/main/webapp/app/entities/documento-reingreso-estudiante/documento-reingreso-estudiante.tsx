import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocumentoReingresoEstudiante } from 'app/shared/model/documento-reingreso-estudiante.model';
import { getEntities } from './documento-reingreso-estudiante.reducer';

export const DocumentoReingresoEstudiante = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const documentoReingresoEstudianteList = useAppSelector(state => state.documentoReingresoEstudiante.entities);
  const loading = useAppSelector(state => state.documentoReingresoEstudiante.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="documento-reingreso-estudiante-heading" data-cy="DocumentoReingresoEstudianteHeading">
        <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.home.title">Documento Reingreso Estudiantes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/documento-reingreso-estudiante/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.home.createLabel">
              Create new Documento Reingreso Estudiante
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {documentoReingresoEstudianteList && documentoReingresoEstudianteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.nombreDocumento">Nombre Documento</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.descripcionDocumento">Descripcion Documento</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.solicitudReingreso">Solicitud Reingreso</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {documentoReingresoEstudianteList.map((documentoReingresoEstudiante, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/documento-reingreso-estudiante/${documentoReingresoEstudiante.id}`} color="link" size="sm">
                      {documentoReingresoEstudiante.id}
                    </Button>
                  </td>
                  <td>{documentoReingresoEstudiante.nombreDocumento}</td>
                  <td>{documentoReingresoEstudiante.descripcionDocumento}</td>
                  <td>
                    {documentoReingresoEstudiante.solicitudReingreso ? (
                      <Link to={`/solicitud-reingreso/${documentoReingresoEstudiante.solicitudReingreso.id}`}>
                        {documentoReingresoEstudiante.solicitudReingreso.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/documento-reingreso-estudiante/${documentoReingresoEstudiante.id}`}
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
                        to={`/documento-reingreso-estudiante/${documentoReingresoEstudiante.id}/edit`}
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
                        to={`/documento-reingreso-estudiante/${documentoReingresoEstudiante.id}/delete`}
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
              <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.home.notFound">
                No Documento Reingreso Estudiantes found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DocumentoReingresoEstudiante;
