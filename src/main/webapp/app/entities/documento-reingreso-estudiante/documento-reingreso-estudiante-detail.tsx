import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './documento-reingreso-estudiante.reducer';

export const DocumentoReingresoEstudianteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentoReingresoEstudianteEntity = useAppSelector(state => state.documentoReingresoEstudiante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentoReingresoEstudianteDetailsHeading">
          <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.detail.title">DocumentoReingresoEstudiante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.id">Id</Translate>
            </span>
          </dt>
          <dd>{documentoReingresoEstudianteEntity.id}</dd>
          <dt>
            <span id="nombreDocumento">
              <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.nombreDocumento">Nombre Documento</Translate>
            </span>
          </dt>
          <dd>{documentoReingresoEstudianteEntity.nombreDocumento}</dd>
          <dt>
            <span id="descripcionDocumento">
              <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.descripcionDocumento">Descripcion Documento</Translate>
            </span>
          </dt>
          <dd>{documentoReingresoEstudianteEntity.descripcionDocumento}</dd>
          <dt>
            <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.solicitudReingreso">Solicitud Reingreso</Translate>
          </dt>
          <dd>{documentoReingresoEstudianteEntity.solicitudReingreso ? documentoReingresoEstudianteEntity.solicitudReingreso.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/documento-reingreso-estudiante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/documento-reingreso-estudiante/${documentoReingresoEstudianteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentoReingresoEstudianteDetail;
