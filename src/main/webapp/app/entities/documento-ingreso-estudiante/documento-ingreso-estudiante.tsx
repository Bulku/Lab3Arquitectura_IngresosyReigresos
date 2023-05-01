import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocumentoIngresoEstudiante } from 'app/shared/model/documento-ingreso-estudiante.model';
import { getEntities } from './documento-ingreso-estudiante.reducer';

export const DocumentoIngresoEstudiante = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const documentoIngresoEstudianteList = useAppSelector(state => state.documentoIngresoEstudiante.entities);
  const loading = useAppSelector(state => state.documentoIngresoEstudiante.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="documento-ingreso-estudiante-heading" data-cy="DocumentoIngresoEstudianteHeading">
        <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.home.title">Documento Ingreso Estudiantes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/documento-ingreso-estudiante/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.home.createLabel">
              Create new Documento Ingreso Estudiante
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {documentoIngresoEstudianteList && documentoIngresoEstudianteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.nombreDocumento">Nombre Documento</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.descripcionDocumento">Descripcion Documento</Translate>
                </th>
                <th>
                  <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.ingresoEstudiante">Ingreso Estudiante</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {documentoIngresoEstudianteList.map((documentoIngresoEstudiante, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/documento-ingreso-estudiante/${documentoIngresoEstudiante.id}`} color="link" size="sm">
                      {documentoIngresoEstudiante.id}
                    </Button>
                  </td>
                  <td>{documentoIngresoEstudiante.nombreDocumento}</td>
                  <td>{documentoIngresoEstudiante.descripcionDocumento}</td>
                  <td>
                    {documentoIngresoEstudiante.ingresoEstudiante ? (
                      <Link to={`/ingreso-estudiante/${documentoIngresoEstudiante.ingresoEstudiante.id}`}>
                        {documentoIngresoEstudiante.ingresoEstudiante.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/documento-ingreso-estudiante/${documentoIngresoEstudiante.id}`}
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
                        to={`/documento-ingreso-estudiante/${documentoIngresoEstudiante.id}/edit`}
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
                        to={`/documento-ingreso-estudiante/${documentoIngresoEstudiante.id}/delete`}
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
              <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.home.notFound">
                No Documento Ingreso Estudiantes found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DocumentoIngresoEstudiante;
