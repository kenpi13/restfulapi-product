package com.example.restfulapiproduct.exception;

import com.example.restfulapiproduct.controller.ProductController;
import com.example.restfulapiproduct.dto.ErrorResponseDto;
import com.example.restfulapiproduct.setting.FileSizeSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/** Api例外ハンドラー */
@RestControllerAdvice(assignableTypes = ProductController.class)
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  private final FileSizeSetting fileSizeSetting;
  /**
   * BadRequestException URLが不正だった時
   *
   * @param ex 例外インスタンス
   * @param headers ヘッダー
   * @param status ステータス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestException(
      BadRequestException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ErrorResponseDto responseDto = new ErrorResponseDto("設定した値が不正です", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, headers, status, request);
  }

  /**
   * 同じタイトルがあったときの例外処理
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = ProductAlreadyExistException.class)
  public ResponseEntity<Object> handleProductAlreadyException(
      ProductAlreadyExistException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponseDto responseDto =
        new ErrorResponseDto("同名のtitleが既に存在しています。変更が必要です。", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /**
   * 登録時のバリデーションエラー
   *
   * <p>{@inheritDoc}
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.warn(ex.getMessage(), ex);
    status = HttpStatus.BAD_REQUEST;
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
            .collect(Collectors.toList());
    ErrorResponseDto response = new ErrorResponseDto("不正な入力値です", errors);
    return super.handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }

  /**
   * MethodArgumentTypeMismatch 400エラー
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponseDto responseDto =
        new ErrorResponseDto("リクエストパラメータに不正な書式が設定されています", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /** リクエストボディの中身が不正な値だった時 {@inheritDoc} */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ErrorResponseDto response = new ErrorResponseDto("リクエストボディの書式が不正です", ex.getMessage());
    return super.handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }

  /** MissingServletRequestParameter 検索時に不正なParameterが指定された場合の例外処理 {@inheritDoc} */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ErrorResponseDto responseDto = new ErrorResponseDto("URLにパラメータが含まれていません", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /**
   * 画像処理系共通例外（読み込み・保存）
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = ImageNotUploadException.class)
  public ResponseEntity<Object> handleImageNotUploadException(
      ImageNotUploadException ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponseDto responseDto = new ErrorResponseDto("画像処理でエラーが発生しています", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /**
   * 画像が見つからなかった場合の例外処理
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = ImageNotFoundException.class)
  public ResponseEntity<Object> handleImageNotFoundException(
      ImageNotFoundException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponseDto responseDto = new ErrorResponseDto("画像が見つかりませんでした", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /**
   * 商品が見つからなかった時
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = ProductNotFoundException.class)
  public ResponseEntity<Object> productNotFound(ProductNotFoundException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorResponseDto responseDto = new ErrorResponseDto("商品が見つかりません", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /** 非許可なHTTPメソッドを受け取ったExceptionクラス 405 Method Not Allowed {@inheritDoc} */
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ErrorResponseDto responseDto =
        new ErrorResponseDto("許可されていないHTTPメソッドが指定されました", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /** 存在しないURL 404 NOT FOUND {@inheritDoc} */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    ErrorResponseDto responseDto = new ErrorResponseDto("対象のURLは存在しません", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }

  /**
   * 拡張子不正・拡張子偽装 415 Unsupported Media Type
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = UnsupportedMediaTypeException.class)
  public ResponseEntity<Object> handleUnsupportedMediaTypeException(
      UnsupportedMediaTypeException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    ErrorResponseDto response = new ErrorResponseDto("この拡張子は使用できません", ex.getMessage());
    return super.handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }

  /**
   * 画像サイズが上限を超えた場合
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = MaxUploadSizeExceededException.class)
  public ResponseEntity<Object> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException ex, WebRequest request) {
    log.warn(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorResponseDto response =
        new ErrorResponseDto(
            "設定可能なファイルサイズを超過しています。ファイルサイズは" + fileSizeSetting.getMaxFileSize() + "以内に設定してください",
            ex.getMessage());
    return super.handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
  }

  /**
   * その他すべてのエラーに対する例外処理 500 handleAllException
   *
   * @param ex 例外インスタンス
   * @param request リクエスト
   * @return エラー内容のレスポンス
   */
  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponseDto responseDto = new ErrorResponseDto("サーバーでエラーが生じています", ex.getMessage());
    return super.handleExceptionInternal(ex, responseDto, new HttpHeaders(), status, request);
  }
}
