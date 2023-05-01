import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estudiante.reducer';

export const EstudianteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const estudianteEntity = useAppSelector(state => state.estudiante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="estudianteDetailsHeading">
          <Translate contentKey="arqui3RealApp.estudiante.detail.title">Estudiante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="arqui3RealApp.estudiante.id">Id</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="arqui3RealApp.estudiante.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="arqui3RealApp.estudiante.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.apellido}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="arqui3RealApp.estudiante.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {estudianteEntity.fechaNacimiento ? (
              <TextFormat value={estudianteEntity.fechaNacimiento} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="correo">
              <Translate contentKey="arqui3RealApp.estudiante.correo">Correo</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.correo}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="arqui3RealApp.estudiante.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.direccion}</dd>
        </dl>
        <Button tag={Link} to="/estudiante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estudiante/${estudianteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EstudianteDetail;
