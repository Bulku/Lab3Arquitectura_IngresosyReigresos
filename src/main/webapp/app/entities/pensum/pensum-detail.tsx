import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pensum.reducer';

export const PensumDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pensumEntity = useAppSelector(state => state.pensum.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pensumDetailsHeading">
          <Translate contentKey="arqui3RealApp.pensum.detail.title">Pensum</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="arqui3RealApp.pensum.id">Id</Translate>
            </span>
          </dt>
          <dd>{pensumEntity.id}</dd>
          <dt>
            <span id="numero">
              <Translate contentKey="arqui3RealApp.pensum.numero">Numero</Translate>
            </span>
          </dt>
          <dd>{pensumEntity.numero}</dd>
          <dt>
            <Translate contentKey="arqui3RealApp.pensum.materias">Materias</Translate>
          </dt>
          <dd>
            {pensumEntity.materias
              ? pensumEntity.materias.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {pensumEntity.materias && i === pensumEntity.materias.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/pensum" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pensum/${pensumEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PensumDetail;
