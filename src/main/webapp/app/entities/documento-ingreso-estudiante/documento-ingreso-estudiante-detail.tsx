import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './documento-ingreso-estudiante.reducer';

export const DocumentoIngresoEstudianteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentoIngresoEstudianteEntity = useAppSelector(state => state.documentoIngresoEstudiante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentoIngresoEstudianteDetailsHeading">
          <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.detail.title">DocumentoIngresoEstudiante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.id">Id</Translate>
            </span>
          </dt>
          <dd>{documentoIngresoEstudianteEntity.id}</dd>
          <dt>
            <span id="nombreDocumento">
              <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.nombreDocumento">Nombre Documento</Translate>
            </span>
          </dt>
          <dd>{documentoIngresoEstudianteEntity.nombreDocumento}</dd>
          <dt>
            <span id="descripcionDocumento">
              <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.descripcionDocumento">Descripcion Documento</Translate>
            </span>
          </dt>
          <dd>{documentoIngresoEstudianteEntity.descripcionDocumento}</dd>
          <dt>
            <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.ingresoEstudiante">Ingreso Estudiante</Translate>
          </dt>
          <dd>{documentoIngresoEstudianteEntity.ingresoEstudiante ? documentoIngresoEstudianteEntity.ingresoEstudiante.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/documento-ingreso-estudiante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/documento-ingreso-estudiante/${documentoIngresoEstudianteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentoIngresoEstudianteDetail;
